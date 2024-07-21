package org.musinsa.exhibition.presentation.dto

data class ProductUpdateRequestDto(
    val productId: Long,
    val brandId: Long,
    val category: String,
    val price: Long
)
