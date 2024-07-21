package org.musinsa.exhibition.domain.product.exception

open class CommonException(
    val value: String = "",
    message: String = "심각한 오류 입니다."
) : RuntimeException(message)



