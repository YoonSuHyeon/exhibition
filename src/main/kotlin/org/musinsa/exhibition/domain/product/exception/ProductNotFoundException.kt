package org.musinsa.exhibition.domain.product.exception


class ProductNotFoundException(value: String) : CommonException(value, "존재 하지 않는 상품 입니다.")
