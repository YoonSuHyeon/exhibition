package org.musinsa.exhibition.domain.product.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.musinsa.exhibition.domain.product.entity.Brand
import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.exception.CategoryNotFoundException
import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.domain.product.model.ProductEvent
import org.musinsa.exhibition.infrastructure.persistence.ProductRepository
import org.musinsa.exhibition.presentation.dto.ProductDeleteRequestDto
import org.musinsa.exhibition.presentation.dto.ProductInsertRequestDto
import org.musinsa.exhibition.presentation.dto.ProductUpdateRequestDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals

class ProductServiceTest {
    private val productRepository: ProductRepository = mockk()
    private val brandService: BrandService = mockk()
    private val applicationEventPublisher: ApplicationEventPublisher = mockk()
    private val productService = ProductService(productRepository, brandService, applicationEventPublisher)

    @Test
    fun insert() {
        val productInsertRequestDto = ProductInsertRequestDto(brandId = 1, category = Category.BAG.categoryName, 100)
        val slot = slot<Product>()
        val slot2 = slot<ProductEvent>()

        val brand = Brand(1, "TEST1")
        every { brandService.getBrandAndThrow(productInsertRequestDto.brandId) } returns brand
        every { productRepository.save(capture(slot)) } answers {
            slot.captured.apply { productId = 1 }
        }
        every { applicationEventPublisher.publishEvent(capture(slot2)) } returns Unit

        productService.insert(productInsertRequestDto)

        assertEquals(1, slot.captured.productId)
        assertEquals(productInsertRequestDto.brandId, slot.captured.brand.brandId)
        assertEquals(productInsertRequestDto.category, slot.captured.category.categoryName)
        assertEquals(productInsertRequestDto.price, slot.captured.price)

        assertEquals(slot.captured, slot2.captured.product)
    }

    @Test
    fun insertThrowsException() {
        val productInsertRequestDto = ProductInsertRequestDto(1, "미존재 카테고리", 100)

        val brand = Brand(1, "TEST1")
        every { brandService.getBrandAndThrow(productInsertRequestDto.brandId) } returns brand

        val categoryNotFoundException =
            assertThrows<CategoryNotFoundException> { productService.insert(productInsertRequestDto) }

        assertEquals(productInsertRequestDto.category, categoryNotFoundException.value)
    }

    @Test
    fun update() {
        val productUpdateRequestDto = ProductUpdateRequestDto(1, 2, Category.BAG.categoryName, 100)
        val slot = slot<Product>()
        val slot2 = slot<ProductEvent>()

        val brand = Brand(1, "TEST1")
        val updateBrand = Brand(2, "TEST2")

        val product = Product(1, brand, Category.CAP, 300)
        every { productRepository.findByIdOrNull(product.productId) } returns product
        every { brandService.getBrandAndThrow(productUpdateRequestDto.brandId) } returns updateBrand
        every { productRepository.save(capture(slot)) } answers {
            slot.captured
        }

        every { applicationEventPublisher.publishEvent(capture(slot2)) } returns Unit

        productService.update(productUpdateRequestDto)

        assertEquals(1, slot.captured.productId)
        assertEquals(productUpdateRequestDto.brandId, slot.captured.brand.brandId)
        assertEquals(productUpdateRequestDto.category, slot.captured.category.categoryName)
        assertEquals(productUpdateRequestDto.price, slot.captured.price)

        assertEquals(slot.captured, slot2.captured.product)
    }

    @Test
    fun updateThrowsException() {
        val productUpdateRequestDto = ProductUpdateRequestDto(1, 2, "미존재 카테고리", 100)

        val brand = Brand(1, "TEST1")
        val product = Product(1, brand, Category.CAP, 300)
        val updateBrand = Brand(2, "TEST2")

        every { productRepository.findByIdOrNull(product.productId) } returns product
        every { brandService.getBrandAndThrow(productUpdateRequestDto.brandId) } returns updateBrand

        val categoryNotFoundException =
            assertThrows<CategoryNotFoundException> { productService.update(productUpdateRequestDto) }

        assertEquals(productUpdateRequestDto.category, categoryNotFoundException.value)
    }

    @Test
    fun delete() {
        val productDeleteRequestDto = ProductDeleteRequestDto(1)
        val slot = slot<Product>()
        val slot2 = slot<ProductEvent>()

        val brand = Brand(1, "TEST1")
        val product = Product(1, brand, Category.CAP, 300)

        every { productRepository.findByIdOrNull(product.productId) } returns product
        every { productRepository.delete(capture(slot)) } answers {
            slot.captured
        }
        every { applicationEventPublisher.publishEvent(capture(slot2)) } returns Unit

        productService.delete(productDeleteRequestDto)

        assertEquals(1, slot.captured.productId)
        assertEquals(product.brand.brandId, slot.captured.brand.brandId)
        assertEquals(product.category, slot.captured.category)
        assertEquals(product.price, slot.captured.price)

        assertEquals(slot.captured, slot2.captured.product)
    }
}
