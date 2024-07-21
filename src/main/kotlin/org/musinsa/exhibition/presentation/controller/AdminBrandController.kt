package org.musinsa.exhibition.presentation.controller

import org.musinsa.exhibition.domain.product.service.BrandService
import org.musinsa.exhibition.presentation.dto.BrandDeleteRequestDto
import org.musinsa.exhibition.presentation.dto.BrandInsertRequestDto
import org.musinsa.exhibition.presentation.dto.BrandResponseDto
import org.musinsa.exhibition.presentation.dto.BrandUpdateRequestDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/admin/brands")
class AdminBrandController(private val brandService: BrandService) {

    @GetMapping
    fun getAll(): BrandResponseDto = brandService.getBrands()

    @PostMapping
    fun insertBrand(@RequestBody brandInsertRequestDto: BrandInsertRequestDto) =
        brandService.insert(brandInsertRequestDto)

    @PutMapping
    fun updateBrand(@RequestBody brandUpdateRequestDto: BrandUpdateRequestDto) =
        brandService.update(brandUpdateRequestDto)

    @DeleteMapping
    fun deleteBrand(@RequestBody brandDeleteRequestDto: BrandDeleteRequestDto) =
        brandService.delete(brandDeleteRequestDto)
}
