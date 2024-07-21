package org.musinsa.exhibition.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.infrastructure.utils.PriceFormatUtils

data class MinMaxPriceByCategoryInfo(
    @JsonProperty("카테고리")
    val category: String,
    @JsonProperty("최저가")
    val minPriceProducts: List<PriceProduct>,
    @JsonProperty("최고가")
    val maxPriceProducts: List<PriceProduct>
) {
    companion object {
        fun of(
            category: Category,
            minPriceProducts: List<Product>,
            maxPriceProducts: List<Product>
        ): MinMaxPriceByCategoryInfo {
            return MinMaxPriceByCategoryInfo(
                category = category.categoryName,
                minPriceProducts = minPriceProducts.map(PriceProduct::from),
                maxPriceProducts = maxPriceProducts.map(PriceProduct::from)
            )
        }

    }
}

data class PriceProduct(@JsonProperty("브랜드") val brand: String, @JsonProperty("가격") val price: String) {
    companion object {
        fun from(product: Product): PriceProduct {
            return PriceProduct(product.brand.name, PriceFormatUtils.format(product.price))
        }
    }
}
