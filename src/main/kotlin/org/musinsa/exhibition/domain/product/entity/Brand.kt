package org.musinsa.exhibition.domain.product.entity

import jakarta.persistence.*

@Entity
@Table(name = "brand")
class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var brandId: Long? = null,

    var name: String
)
