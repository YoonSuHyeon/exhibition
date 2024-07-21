package org.musinsa.exhibition.infrastructure.persistence

import org.musinsa.exhibition.domain.product.entity.Product
import org.musinsa.exhibition.domain.product.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByProductIdIn(productIds: List<Long>): List<Product>

    fun findAllByBrand_BrandId(brandId: Long): List<Product>

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price = (SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = :category)")
    fun findAllByMinPriceProductByCategory(@Param("category") category: Category): List<Product>

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price = (SELECT MAX(p2.price) FROM Product p2 WHERE p2.category = :category)")
    fun findAllByMaxPriceProductByCategory(@Param("category") category: Category): List<Product>

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.brand.brandId = :brandId AND p.price = (SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = :category and p2.brand.brandId = :brandId)")
    fun findAllByMinPriceProductsByBrandAndCategory(
        @Param("brandId") brandId: Long,
        @Param("category") category: Category
    ): List<Product>

}


