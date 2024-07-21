package org.musinsa.exhibition.infrastructure.cache

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentSkipListMap

@Component
class CustomCacheUtils {

    // 카테고리별 최저가격 productId
    private val minPriceProductByCategoryMap: ConcurrentMap<String, List<*>> = ConcurrentHashMap()

    // 카테고리별 최고 productId
    private val maxPriceProductByCategoryMap: ConcurrentMap<String, List<*>> = ConcurrentHashMap()

    // 브랜드별 전체 카테고리 최저가 상품을 구매한 경우 가격 총합 (최신순) key : brandId , value : priceSum
    private val minBrandAllCategoryProductPriceSumMap: ConcurrentSkipListMap<Long, Long> = ConcurrentSkipListMap()

    // 브랜드_카테고리 최저가격 productId
    private val minBrandCategoryProductPriceMap: ConcurrentMap<String, List<*>> = ConcurrentHashMap()

    fun get(type: CustomCacheType, key: String? = null): Any? {
        return when (type) {
            CustomCacheType.MIN_PRICE_PRODUCT_IDS_CATEGORY -> minPriceProductByCategoryMap[key]
            CustomCacheType.MAX_PRICE_PRODUCT_IDS_CATEGORY -> maxPriceProductByCategoryMap[key]
            CustomCacheType.SUM_MIN_PRICE_BRAND -> minBrandAllCategoryProductPriceSumMap.entries.minByOrNull { it.value }?.key

            CustomCacheType.MIN_PRICE_PRODUCT_IDS_BRAND_AND_CATEGORY -> minBrandCategoryProductPriceMap[key]
        }
    }

    fun merge(type: CustomCacheType, key: String, value: Any?) {
        when (type) {
            CustomCacheType.MIN_PRICE_PRODUCT_IDS_CATEGORY -> minPriceProductByCategoryMap[key] = value as List<*>
            CustomCacheType.MAX_PRICE_PRODUCT_IDS_CATEGORY -> maxPriceProductByCategoryMap[key] = value as List<*>
            CustomCacheType.SUM_MIN_PRICE_BRAND -> minBrandAllCategoryProductPriceSumMap[key.toLong()] =
                value as Long

            CustomCacheType.MIN_PRICE_PRODUCT_IDS_BRAND_AND_CATEGORY -> minBrandCategoryProductPriceMap[key] =
                value as List<*>
        }
    }

}
