package org.musinsa.exhibition.domain.product.exception

class CategoryNotFoundException(value: String) : CommonException(value, "존재 하지 않는 카테고리 입니다.")
