package org.musinsa.exhibition.domain.product.exception

class DuplicateBrandNameException(value: String) : CommonException(value, "이미 존재하는 브랜드 입니다.")

