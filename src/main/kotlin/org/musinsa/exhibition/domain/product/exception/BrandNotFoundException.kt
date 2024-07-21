package org.musinsa.exhibition.domain.product.exception


class BrandNotFoundException(value: String) : CommonException(value, "존재 하지 않는 브랜드 입니다.")
