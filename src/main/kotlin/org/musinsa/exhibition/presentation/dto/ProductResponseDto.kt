package org.musinsa.exhibition.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.musinsa.exhibition.domain.product.entity.Product


data class ProductResponseDto(@JsonProperty("상품 정보") val brands: List<ProductResponseInfo>) {
    companion object {
        fun from(allBrands: List<Product>): ProductResponseDto {
            val info = allBrands.map(ProductResponseInfo::from)
            return ProductResponseDto(info)
        }
    }
}

data class ProductResponseInfo(
    var productId: Long,
    @JsonProperty("브랜드")
    var brandName: String,
    @JsonProperty("카테고리")
    var category: String,
    @JsonProperty("가격")
    var price: Long
) {
    companion object {
        fun from(product: Product): ProductResponseInfo {
            return ProductResponseInfo(
                productId = product.productId ?: 0,
                brandName = product.brand.name,
                category = product.category.categoryName,
                price = product.price
            )
        }
    }
}
