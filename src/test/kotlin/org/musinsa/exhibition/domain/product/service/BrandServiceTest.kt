package org.musinsa.exhibition.domain.product.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.musinsa.exhibition.domain.product.entity.Brand
import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.exception.BrandDeleteException
import org.musinsa.exhibition.domain.product.exception.BrandNotFoundException
import org.musinsa.exhibition.domain.product.exception.DuplicateBrandNameException
import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.infrastructure.persistence.BrandRepository
import org.musinsa.exhibition.infrastructure.persistence.ProductRepository
import org.musinsa.exhibition.presentation.dto.BrandDeleteRequestDto
import org.musinsa.exhibition.presentation.dto.BrandInsertRequestDto
import org.musinsa.exhibition.presentation.dto.BrandUpdateRequestDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class)
class BrandServiceTest {

    private val brandRepository: BrandRepository = mockk()
    private val productRepository: ProductRepository = mockk()
    private val brandService = BrandService(brandRepository, productRepository)

    @Test
    fun insert() {
        val brandInsertRequestDto = BrandInsertRequestDto("Test1")

        val slot = slot<Brand>()
        every { brandRepository.existsByName(brandInsertRequestDto.name) } returns false
        every { brandRepository.save(capture(slot)) } answers {
            slot.captured.apply { brandId = 1 }
        }

        brandService.insert(brandInsertRequestDto)

        verify { brandRepository.save(any()) }
        assert(slot.captured.name == brandInsertRequestDto.name)
    }

    @Test
    fun insertThrowsException() {
        val brandInsertRequestDto = BrandInsertRequestDto("Test1")

        every { brandRepository.existsByName(brandInsertRequestDto.name) } returns true

        val duplicateBrandNameException = assertThrows<DuplicateBrandNameException> {
            brandService.insert(brandInsertRequestDto)
        }

        assertEquals(brandInsertRequestDto.name, duplicateBrandNameException.value)
    }

    @Test
    fun update() {
        val brandUpdateRequestDto = BrandUpdateRequestDto(1, "Test2")

        val slot = slot<Brand>()
        every { brandRepository.existsByName(brandUpdateRequestDto.name) } returns false
        val brand = Brand(brandUpdateRequestDto.brandId, "Test1")
        every { brandRepository.findByIdOrNull(brandUpdateRequestDto.brandId) } returns brand

        every { brandRepository.save(capture(slot)) } answers {
            slot.captured
        }

        brandService.update(brandUpdateRequestDto)

        assertEquals(brandUpdateRequestDto.name, slot.captured.name)
    }

    @Test
    fun updateThrowsException() {
        val brandUpdateRequestDto = BrandUpdateRequestDto(1, "Test2")

        every { brandRepository.existsByName(brandUpdateRequestDto.name) } returns true

        val duplicateBrandNameException = assertThrows<DuplicateBrandNameException> {
            brandService.update(brandUpdateRequestDto)
        }
        assertEquals(brandUpdateRequestDto.name, duplicateBrandNameException.value)

        every { brandRepository.existsByName(brandUpdateRequestDto.name) } returns false
        every { brandRepository.findByIdOrNull(brandUpdateRequestDto.brandId) } returns null
        val brandNotFoundException = assertThrows<BrandNotFoundException> {
            brandService.update(brandUpdateRequestDto)
        }

        assertEquals(brandUpdateRequestDto.brandId.toString(), brandNotFoundException.value)

    }


    @Test
    fun delete() {
        val brandDeleteRequestDto = BrandDeleteRequestDto(1)
        val slot = slot<Brand>()

        val brand = Brand(1, "Test")
        every { brandRepository.findByIdOrNull(brandDeleteRequestDto.brandId) } returns brand
        every { brandRepository.delete(capture(slot)) } returns Unit
        every { productRepository.findAllByBrand_BrandId(1) } returns emptyList()

        brandService.delete(brandDeleteRequestDto)

        assertEquals(slot.captured.brandId, brandDeleteRequestDto.brandId)
        assertEquals(slot.captured.name, brand.name)
    }

    @Test
    fun deleteThrowsException() {
        val brandDeleteRequestDto = BrandDeleteRequestDto(1)

        every { brandRepository.findByIdOrNull(brandDeleteRequestDto.brandId) } returns null
        val brandNotFoundException = assertThrows<BrandNotFoundException> { brandService.delete(brandDeleteRequestDto) }
        assertEquals(brandDeleteRequestDto.brandId.toString(), brandNotFoundException.value)

        val brand = Brand(1, "Test")
        val products = listOf(Product(1, brand, Category.BAG, 100))
        every { brandRepository.findByIdOrNull(brandDeleteRequestDto.brandId) } returns brand
        every { productRepository.findAllByBrand_BrandId(1) } returns products

        val brandDeleteException = assertThrows<BrandDeleteException> { brandService.delete(brandDeleteRequestDto) }

        assertEquals(products.mapNotNull { it.productId }.toString(), brandDeleteException.value)
    }

}
