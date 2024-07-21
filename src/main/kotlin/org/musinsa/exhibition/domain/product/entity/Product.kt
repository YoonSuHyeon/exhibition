package org.musinsa.exhibition.domain.product.entity

import jakarta.persistence.*
import org.musinsa.exhibition.domain.product.model.Category


@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    var brand: Brand,

    @Enumerated(EnumType.STRING)
    var category: Category,

    var price: Long
)
