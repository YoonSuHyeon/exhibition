package org.musinsa.exhibition.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.infrastructure.utils.PriceFormatUtils

data class MinPriceByCategoryInfo(
    @JsonProperty("총액") val totalPrice: String,
    @JsonProperty("상품") val products: List<MinPriceProduct>
) {

    companion object {
        private const val EMPTY_PRODUCT_BRAND_NAME = "없음"

        fun of(categoryWithProductId: Map<Category, List<Long>>, products: List<Product>): MinPriceByCategoryInfo {
            val productMap = products.associateBy { it.productId }

            var totalPrice: Long = 0
            val minPriceProducts = categoryWithProductId.map {
                // 브랜드 이름 순
                val productOrNull =
                    productMap.filter { e -> it.value.contains(e.key) }.values.maxByOrNull { v -> v.brand.name }

                val price = productOrNull?.price ?: 0
                totalPrice += price

                MinPriceProduct(
                    it.key.categoryName,
                    productOrNull?.brand?.name ?: EMPTY_PRODUCT_BRAND_NAME,
                    PriceFormatUtils.format(price)
                )
            }

            return MinPriceByCategoryInfo(totalPrice = PriceFormatUtils.format(totalPrice), products = minPriceProducts)
        }
    }
}

data class MinPriceProduct(
    @JsonProperty("카테고리")
    val category: String,
    @JsonProperty("브랜드")
    val brand: String,
    @JsonProperty("가격")
    val price: String
)
