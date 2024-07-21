package org.musinsa.exhibition.infrastructure.handler

import org.musinsa.exhibition.domain.product.exception.CommonException
import org.musinsa.exhibition.presentation.dto.ApiErrorResponseDto
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(CommonException::class)
    fun handleApiException(ex: CommonException): ApiErrorResponseDto {
        val response = ApiErrorResponseDto(
            value = ex.value,
            message = ex.message ?: ""
        )
        return response
    }

}
