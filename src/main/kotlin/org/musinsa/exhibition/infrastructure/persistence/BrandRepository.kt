package org.musinsa.exhibition.infrastructure.persistence

import org.musinsa.exhibition.domain.product.entity.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<Brand, Long> {
    fun existsByName(name: String): Boolean
}
