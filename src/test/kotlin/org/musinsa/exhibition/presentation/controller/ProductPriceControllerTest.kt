package org.musinsa.exhibition.presentation.controller

import org.junit.jupiter.api.Test
import org.musinsa.exhibition.domain.product.service.ProductService
import org.musinsa.exhibition.presentation.dto.CategoryProductPrice
import org.musinsa.exhibition.presentation.dto.MinPriceProduct
import org.musinsa.exhibition.presentation.dto.PriceProduct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

/**
 * TestCommandLineRunner 실행 기준 진행
 */
@SpringBootTest
class ProductPriceControllerTest {

    @Autowired
    private lateinit var productPriceController: ProductPriceController

    @Autowired
    private lateinit var productService: ProductService

    @Test
    fun getMinPriceByCategoryInfo() {
        val expectedTotalPrice = "34,100"
        val expectedProductMap = mapOf(
            "상의" to MinPriceProduct(category = "상의", brand = "C", price = "10,000"),
            "아우터" to MinPriceProduct(category = "아우터", brand = "E", price = "5,000"),
            "바지" to MinPriceProduct(category = "바지", brand = "D", price = "3,000"),
            "스니커즈" to MinPriceProduct(category = "스니커즈", brand = "G", price = "9,000"),
            "가방" to MinPriceProduct(category = "가방", brand = "A", price = "2,000"),
            "모자" to MinPriceProduct(category = "모자", brand = "D", price = "1,500"),
            "양말" to MinPriceProduct(category = "양말", brand = "I", price = "1,700"),
            "액세서리" to MinPriceProduct(category = "액세서리", brand = "F", price = "1,900"),
        )

        val minPriceByCategoryInfo = productPriceController.getMinPriceByCategoryInfo()

        assertEquals(expectedTotalPrice, minPriceByCategoryInfo.totalPrice)
        minPriceByCategoryInfo.products.forEach {
            val expected = expectedProductMap[it.category]
            assertEquals(expected?.category, it.category)
            assertEquals(expected?.brand, it.brand)
            assertEquals(expected?.price, it.price)
        }

    }

    @Test
    fun getProductPriceSumByBrandInfo() {
        val expectedBrand = "D"
        val expectedTotalPrice = "36,100"
        val expectedProductMap = mapOf(
            "상의" to CategoryProductPrice("상의", "10,100"),
            "아우터" to CategoryProductPrice("아우터", "5,100"),
            "바지" to CategoryProductPrice("바지", "3,000"),
            "스니커즈" to CategoryProductPrice("스니커즈", "9,500"),
            "가방" to CategoryProductPrice("가방", "2,500"),
            "모자" to CategoryProductPrice("모자", "1,500"),
            "양말" to CategoryProductPrice("양말", "2,400"),
            "액세서리" to CategoryProductPrice("액세서리", "2,000"),
        )

        val productPriceSumByBrandInfo = productPriceController.getProductPriceSumByBrandInfo()

        assertEquals(expectedBrand, productPriceSumByBrandInfo.minProductPrices.brand)
        assertEquals(expectedTotalPrice, productPriceSumByBrandInfo.minProductPrices.totalPrice)
        productPriceSumByBrandInfo.minProductPrices.categoryProductPrices.forEach {
            val expected = expectedProductMap[it.category]
            assertEquals(expected?.category, it.category)
            assertEquals(expected?.price, it.price)
        }
    }

    @Test
    fun getMinMaxPriceByCategoryInfo() {
        val expectedCategory = "상의"
        val minPriceProductMap = mapOf(
            "C" to PriceProduct("C", "10,000")
        )
        val maxPriceProductMap = mapOf(
            "I" to PriceProduct("I", "11,400")
        )

        val minMaxPriceByCategoryInfo = productPriceController.getMinMaxPriceByCategoryInfo(expectedCategory)

        assertEquals(expectedCategory, minMaxPriceByCategoryInfo.category)

        minMaxPriceByCategoryInfo.minPriceProducts.forEach {
            val priceProduct = minPriceProductMap[it.brand]
            assertEquals(priceProduct?.price, it.price)
            assertEquals(priceProduct?.brand, it.brand)
        }

        minMaxPriceByCategoryInfo.maxPriceProducts.forEach {
            val priceProduct = maxPriceProductMap[it.brand]
            assertEquals(priceProduct?.price, it.price)
            assertEquals(priceProduct?.brand, it.brand)
        }
    }
}
