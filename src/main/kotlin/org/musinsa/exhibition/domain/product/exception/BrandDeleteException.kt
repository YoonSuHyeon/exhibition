package org.musinsa.exhibition.domain.product.exception

class BrandDeleteException(value: String) : CommonException(value, "상품을 삭제 후 진행 해야 합니다.")
