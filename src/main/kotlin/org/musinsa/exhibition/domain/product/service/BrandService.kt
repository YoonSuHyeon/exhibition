package org.musinsa.exhibition.domain.product.service

import org.musinsa.exhibition.domain.product.entity.Brand
import org.musinsa.exhibition.domain.product.exception.BrandDeleteException
import org.musinsa.exhibition.domain.product.exception.BrandNotFoundException
import org.musinsa.exhibition.domain.product.exception.CommonException
import org.musinsa.exhibition.domain.product.exception.DuplicateBrandNameException
import org.musinsa.exhibition.infrastructure.persistence.BrandRepository
import org.musinsa.exhibition.infrastructure.persistence.ProductRepository
import org.musinsa.exhibition.presentation.dto.BrandDeleteRequestDto
import org.musinsa.exhibition.presentation.dto.BrandInsertRequestDto
import org.musinsa.exhibition.presentation.dto.BrandResponseDto
import org.musinsa.exhibition.presentation.dto.BrandUpdateRequestDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(private val brandRepository: BrandRepository, private val productRepository: ProductRepository) {

    /**
     * 중복 이름을 피하기 위해서 rock 필요한 경우는 추가
     */
    @Transactional
    fun insert(brandInsertRequestDto: BrandInsertRequestDto): Long {
        val brandName = brandInsertRequestDto.name

        if (brandRepository.existsByName(brandName))
            throw DuplicateBrandNameException(brandName)

        val newBrand = Brand(name = brandName)
        brandRepository.save(newBrand)

        return newBrand.brandId ?: throw CommonException()
    }

    /**
     * 중복 이름을 피하기 위해서 rock 필요한 경우는 추가
     */
    @Transactional
    fun update(brandUpdateRequestDto: BrandUpdateRequestDto) {
        val newName = brandUpdateRequestDto.name

        if (brandRepository.existsByName(newName))
            throw DuplicateBrandNameException(newName)

        val brand = getBrandAndThrow(brandUpdateRequestDto.brandId)
        brand.name =newName

        brandRepository.save(brand)
    }

    /**
     * 브랜드 삭제 시 상품을 먼저 삭제 후 가능
     */
    @Transactional
    fun delete(brandDeleteRequestDto: BrandDeleteRequestDto) {
        val brand = getBrandAndThrow(brandDeleteRequestDto.brandId)
        val products = productRepository.findAllByBrand_BrandId(brand.brandId ?: 0)

        if (products.isNotEmpty())
            throw BrandDeleteException(products.mapNotNull { it.productId }.toString())

        brandRepository.delete(brand)
    }

    @Transactional(readOnly = true)
    fun getBrandAndThrow(brandId: Long): Brand {
        return brandRepository.findByIdOrNull(brandId) ?: throw BrandNotFoundException(brandId.toString())
    }

    @Transactional(readOnly = true)
    fun getBrand(brandId: Long): Brand? {
        return brandRepository.findByIdOrNull(brandId)
    }

    @Transactional(readOnly = true)
    fun getBrands(): BrandResponseDto {
        return BrandResponseDto.from(brandRepository.findAll())
    }
}
