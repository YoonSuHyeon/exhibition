package org.musinsa.exhibition.presentation.controller

import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.domain.product.service.ProductPriceService
import org.musinsa.exhibition.presentation.dto.MinMaxPriceByCategoryInfo
import org.musinsa.exhibition.presentation.dto.MinPriceByCategoryInfo
import org.musinsa.exhibition.presentation.dto.ProductPriceSumByBrandInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/v1/products")
class ProductPriceController(private val productPriceService: ProductPriceService) {

    @GetMapping("/min-category-info")
    fun getMinPriceByCategoryInfo(): MinPriceByCategoryInfo = productPriceService.getMinPriceByCategoryInfo()

    @GetMapping("/brand-info")
    fun getProductPriceSumByBrandInfo(): ProductPriceSumByBrandInfo =
        productPriceService.getMinPriceProductSumByBrandInfo()

    @GetMapping("/min-max-category-info")
    fun getMinMaxPriceByCategoryInfo(@RequestParam category: String): MinMaxPriceByCategoryInfo =
        productPriceService.getMinMaxPriceByCategoryInfo(Category.findByCategory(category))
}
