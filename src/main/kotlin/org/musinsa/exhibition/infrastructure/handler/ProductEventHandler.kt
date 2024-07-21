package org.musinsa.exhibition.infrastructure.handler

import org.musinsa.exhibition.domain.product.service.ProductService
import org.musinsa.exhibition.infrastructure.cache.CustomCacheType
import org.musinsa.exhibition.infrastructure.cache.CustomCacheUtils
import org.musinsa.exhibition.domain.product.model.ProductEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.security.InvalidParameterException

@Component
class ProductEventHandler(private val productService: ProductService, private val customCacheUtils: CustomCacheUtils) {

    @EventListener
    fun change(event: ProductEvent) {
        val product = event.product
        val brandId = product.brand.brandId ?: throw InvalidParameterException()
        val category = product.category.name

        val minPriceProductIds = productService.getMinPriceProductIdsByCategory(product.category)
        customCacheUtils.merge(
            CustomCacheType.MIN_PRICE_PRODUCT_IDS_CATEGORY,
            category,
            minPriceProductIds
        )

        val maxPriceProductIds = productService.getMaxPriceProductIdsByCategory(product.category)
        customCacheUtils.merge(
            CustomCacheType.MAX_PRICE_PRODUCT_IDS_CATEGORY,
            category,
            maxPriceProductIds
        )

        val minBrandAllCategoryProductPriceSum =
            productService.getSumOfMinPriceByBrand(brandId)
        customCacheUtils.merge(
            CustomCacheType.SUM_MIN_PRICE_BRAND,
            brandId.toString(),
            minBrandAllCategoryProductPriceSum
        )

        val minPriceProductIdsByBrandAndCategory: List<Long> =
            productService.getMinPriceProductIdsByBrandAndCategory(brandId, product.category)
        customCacheUtils.merge(
            CustomCacheType.MIN_PRICE_PRODUCT_IDS_BRAND_AND_CATEGORY,
            "${brandId}_${category}",
            minPriceProductIdsByBrandAndCategory
        )

    }
}
