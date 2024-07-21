package org.musinsa.exhibition.domain.product.model

import org.musinsa.exhibition.domain.product.entity.Product
import org.springframework.context.ApplicationEvent

class ProductEvent(val product: Product, source: Any) : ApplicationEvent(source)
