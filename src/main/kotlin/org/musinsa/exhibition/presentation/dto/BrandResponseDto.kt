package org.musinsa.exhibition.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.musinsa.exhibition.domain.product.entity.Brand

data class BrandResponseDto(@JsonProperty("브랜드 정보") val brands: List<BrandResponseInfo>) {
    companion object {
        fun from(allBrands: List<Brand>): BrandResponseDto {
            val info = allBrands.map(BrandResponseInfo::from)
            return BrandResponseDto(info)
        }
    }
}

data class BrandResponseInfo(val brandId: Long, @JsonProperty("브랜드") val name: String) {
    companion object {
        fun from(brand: Brand): BrandResponseInfo {
            return BrandResponseInfo(
                brandId = brand.brandId ?: 0,
                name = brand.name
            )
        }
    }
}
