package org.musinsa.exhibition.domain.product.service

import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.exception.ProductNotFoundException
import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.domain.product.model.ProductEvent
import org.musinsa.exhibition.infrastructure.persistence.ProductRepository
import org.musinsa.exhibition.presentation.dto.ProductDeleteRequestDto
import org.musinsa.exhibition.presentation.dto.ProductInsertRequestDto
import org.musinsa.exhibition.presentation.dto.ProductResponseDto
import org.musinsa.exhibition.presentation.dto.ProductUpdateRequestDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val brandService: BrandService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun insert(productInsertRequestDto: ProductInsertRequestDto) {
        val brand = brandService.getBrandAndThrow(productInsertRequestDto.brandId)

        val newProduct =
            Product(
                brand = brand,
                category = Category.findByCategory(productInsertRequestDto.category),
                price = productInsertRequestDto.price
            )
        productRepository.save(newProduct)

        applicationEventPublisher.publishEvent(ProductEvent(newProduct, this))
    }

    @Transactional
    fun update(productUpdateRequestDto: ProductUpdateRequestDto) {
        val product = getOrThrow(productUpdateRequestDto.productId)

        val brand = brandService.getBrandAndThrow(productUpdateRequestDto.brandId)
        product.brand = brand
        product.category = Category.findByCategory(productUpdateRequestDto.category)
        product.price = productUpdateRequestDto.price

        productRepository.save(product)

        applicationEventPublisher.publishEvent(ProductEvent(product, this))
    }

    @Transactional
    fun delete(productDeleteRequestDto: ProductDeleteRequestDto) {
        val product = getOrThrow(productDeleteRequestDto.productId)

        productRepository.delete(product)

        applicationEventPublisher.publishEvent(ProductEvent(product, this))
    }

    @Transactional(readOnly = true)
    fun getOrThrow(productId: Long): Product {
        return productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException(productId.toString())
    }

    @Transactional(readOnly = true)
    fun getProductsByProductIds(productIds: List<Long>): List<Product> {
        if (productIds.isEmpty())
            return emptyList()

        return productRepository.findAllByProductIdIn(productIds)
    }

    @Transactional(readOnly = true)
    fun getProducts(): ProductResponseDto {
        return ProductResponseDto.from(productRepository.findAll())
    }

    @Transactional(readOnly = true)
    fun getMinPriceProductIdsByCategory(category: Category): List<Long> {
        return productRepository.findAllByMinPriceProductByCategory(category).mapNotNull { it.productId }
    }

    @Transactional(readOnly = true)
    fun getMaxPriceProductIdsByCategory(category: Category): List<Long> {
        return productRepository.findAllByMaxPriceProductByCategory(category).mapNotNull { it.productId }
    }

    /**
     * 상품 수가 많아진 경우는 브랜드별 상품 캐싱 필요
     */
    @Transactional(readOnly = true)
    fun getSumOfMinPriceByBrand(brandId: Long): Long {
        val products = productRepository.findAllByBrand_BrandId(brandId)
        val groupBy = products.groupBy { it.category }

        if (groupBy.keys.size != Category.entries.size)
            return Long.MAX_VALUE

        val totalSum = groupBy.entries.sumOf { e -> e.value.minByOrNull { it.price }?.price ?: 0 }
        return totalSum
    }

    @Transactional(readOnly = true)
    fun getMinPriceProductIdsByBrandAndCategory(brandId: Long, category: Category): List<Long> {
        return productRepository.findAllByMinPriceProductsByBrandAndCategory(brandId, category)
            .mapNotNull { it.productId }
    }


}
