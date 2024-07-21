package org.musinsa.exhibition.domain.product.service

import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.infrastructure.cache.CustomCacheType
import org.musinsa.exhibition.infrastructure.cache.CustomCacheUtils
import org.musinsa.exhibition.presentation.dto.MinMaxPriceByCategoryInfo
import org.musinsa.exhibition.presentation.dto.MinPriceByCategoryInfo
import org.musinsa.exhibition.presentation.dto.ProductPriceSumByBrandInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ProductPriceService(
    private val productService: ProductService,
    private val brandService: BrandService,
    private val customCacheUtils: CustomCacheUtils
) {

    @Transactional(readOnly = true)
    fun getMinPriceByCategoryInfo(): MinPriceByCategoryInfo {
        val categoryWithProductIdMap = Category.entries.associateWith {
            (customCacheUtils.get(
                CustomCacheType.MIN_PRICE_PRODUCT_IDS_CATEGORY,
                it.name
            ) as List<*>?)?.mapNotNull { v -> v as Long? } ?: emptyList()
        }

        val productIds = categoryWithProductIdMap.values.flatten().map { it }
        val products = productService.getProductsByProductIds(productIds)

        return MinPriceByCategoryInfo.of(
            categoryWithProductId = categoryWithProductIdMap,
            products = products
        )
    }

    @Transactional(readOnly = true)
    fun getMinPriceProductSumByBrandInfo(): ProductPriceSumByBrandInfo {
        val brandId = customCacheUtils.get(CustomCacheType.SUM_MIN_PRICE_BRAND) as Long?
        val brand = brandService.getBrand(brandId ?: 0)

        val categoryWithProductIdMap = Category.entries.associateWith {
            (customCacheUtils.get(
                CustomCacheType.MIN_PRICE_PRODUCT_IDS_BRAND_AND_CATEGORY,
                "${brandId}_${it.name}"
            ) as List<*>?)?.mapNotNull { v -> v as Long? } ?: emptyList()
        }

        val productIds = categoryWithProductIdMap.values.flatten().map { it }
        val products = productService.getProductsByProductIds(productIds)

        return ProductPriceSumByBrandInfo.of(brand?.name, categoryWithProductIdMap, products)
    }

    @Transactional(readOnly = true)
    fun getMinMaxPriceByCategoryInfo(category: Category): MinMaxPriceByCategoryInfo {
        val minPriceProductIds =
            (customCacheUtils.get(
                CustomCacheType.MIN_PRICE_PRODUCT_IDS_CATEGORY,
                category.name
            ) as List<*>?)?.mapNotNull { it as Long? } ?: emptyList()

        val maxPriceProductIds =
            (customCacheUtils.get(
                CustomCacheType.MAX_PRICE_PRODUCT_IDS_CATEGORY,
                category.name
            ) as List<*>?)?.mapNotNull { it as Long? } ?: emptyList()

        val minPriceProducts = productService.getProductsByProductIds(minPriceProductIds)
        val maxPriceProducts = productService.getProductsByProductIds(maxPriceProductIds)

        return MinMaxPriceByCategoryInfo.of(category, minPriceProducts, maxPriceProducts)
    }

}

