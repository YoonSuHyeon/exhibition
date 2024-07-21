package org.musinsa.exhibition.infrastructure.config

import org.musinsa.exhibition.domain.product.model.Category
import org.musinsa.exhibition.domain.product.service.BrandService
import org.musinsa.exhibition.domain.product.service.ProductService
import org.musinsa.exhibition.presentation.dto.BrandInsertRequestDto
import org.musinsa.exhibition.presentation.dto.ProductInsertRequestDto
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class TestCommandLineRunner(private val brandService: BrandService, private val productService: ProductService) :
    CommandLineRunner {

    override fun run(vararg args: String?) {
        val brandNames = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I")
        val sortedCategories = Category.getSortedValues()
        val priceArrays = arrayOf(
            arrayOf(11200L, 5500L, 4200L, 9000L, 2000L, 1700L, 1800L, 2300L),
            arrayOf(10500L, 5900L, 3800L, 9100L, 2100L, 2000L, 2000L, 2200L),
            arrayOf(10000L, 6200L, 3300L, 9200L, 2200L, 1900L, 2200L, 2100L),
            arrayOf(10100L, 5100L, 3000L, 9500L, 2500L, 1500L, 2400L, 2000L),
            arrayOf(10700L, 5000L, 3800L, 9900L, 2300L, 1800L, 2100L, 2100L),
            arrayOf(11200L, 7200L, 4000L, 9300L, 2100L, 1600L, 2300L, 1900L),
            arrayOf(10500L, 5800L, 3900L, 9000L, 2200L, 1700L, 2100L, 2000L),
            arrayOf(10800L, 6300L, 3100L, 9700L, 2100L, 1600L, 2000L, 2000L),
            arrayOf(11400L, 6700L, 3200L, 9500L, 2400L, 1700L, 1700L, 2400L)
        )
        // 테스트용 브랜드 상품 생성
        brandNames.forEachIndexed { i, name ->
            val brandId = brandService.insert(BrandInsertRequestDto(name))
            val prices = priceArrays[i]
            sortedCategories.forEachIndexed { j, category ->
                productService.insert(ProductInsertRequestDto(brandId, category.categoryName, prices[j]))
            }
        }

    }

}




