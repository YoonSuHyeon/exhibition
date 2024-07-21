package org.musinsa.exhibition.presentation.controller

import org.musinsa.exhibition.domain.product.service.ProductService
import org.musinsa.exhibition.presentation.dto.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/admin/products")
class AdminProductController(private val productService: ProductService) {

    @GetMapping
    fun getAll(): ProductResponseDto = productService.getProducts()

    @PostMapping
    fun insertProduct(@RequestBody productInsertRequestDto: ProductInsertRequestDto) =
        productService.insert(productInsertRequestDto)

    @PutMapping
    fun updateProduct(@RequestBody productUpdateRequestDto: ProductUpdateRequestDto) =
        productService.update(productUpdateRequestDto)

    @DeleteMapping
    fun deleteProduct(@RequestBody productDeleteRequestDto: ProductDeleteRequestDto) =
        productService.delete(productDeleteRequestDto)
}
