package org.musinsa.exhibition.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.infrastructure.utils.PriceFormatUtils

data class ProductPriceSumByBrandInfo(
    @JsonProperty("최저가")
    val minProductPrices: ProductPriceSumByBrand
) {

    companion object {
        private const val EMPTY_PRODUCT_BRAND_NAME = "없음"
        fun of(
            brandName: String?,
            categoryWithProductIdMap: Map<Category, List<Long>>,
            products: List<Product>
        ): ProductPriceSumByBrandInfo {
            val productMap = products.associateBy { it.productId }

            val categoryProductPrices = categoryWithProductIdMap.map {
                it.value.map { v ->
                    CategoryProductPrice(
                        it.key.categoryName,
                        PriceFormatUtils.format(productMap[v]?.price ?: 0)
                    )
                }
            }.flatten()

            val totalPrice = products.sumOf { it.price }
            val productPriceSumByBrand =
                ProductPriceSumByBrand(
                    brandName ?: EMPTY_PRODUCT_BRAND_NAME,
                    categoryProductPrices,
                    PriceFormatUtils.format(totalPrice)
                )

            return ProductPriceSumByBrandInfo(productPriceSumByBrand)
        }
    }
}

data class ProductPriceSumByBrand(
    @JsonProperty("브랜드")
    val brand: String,
    @JsonProperty("카테고리")
    val categoryProductPrices: List<CategoryProductPrice>,
    @JsonProperty("총액")
    val totalPrice: String,
)

data class CategoryProductPrice(
    @JsonProperty("카테고리")
    val category: String,
    @JsonProperty("가격")
    val price: String
)
