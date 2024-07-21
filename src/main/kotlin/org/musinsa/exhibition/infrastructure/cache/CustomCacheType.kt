package org.musinsa.exhibition.infrastructure.cache

enum class CustomCacheType(val description: String) {
    MIN_PRICE_PRODUCT_IDS_CATEGORY("카테고리별 최저 가격 상품 ID 정보"),
    MAX_PRICE_PRODUCT_IDS_CATEGORY("카테고리별 최고 가격 상품 ID 정보"),
    SUM_MIN_PRICE_BRAND("브랜드별 모든 카테고리 최소 가격 총액 정보"),
    MIN_PRICE_PRODUCT_IDS_BRAND_AND_CATEGORY("브랜드와 카테고리별 최소 가격 상품 ID 정보"),
}
