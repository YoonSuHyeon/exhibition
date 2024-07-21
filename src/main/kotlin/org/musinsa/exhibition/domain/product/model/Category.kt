package org.musinsa.exhibition.domain.product.model

import org.musinsa.exhibition.domain.product.exception.CategoryNotFoundException

enum class Category(val categoryName: String, val priority: Int) {
    TOP("상의", 8),
    OUTER("아우터", 7),
    PANTS("바지", 6),
    SNEAKERS("스니커즈", 5),
    BAG("가방", 4),
    CAP("모자", 3),
    SOCKS("양말", 2),
    ACCESSORIES("액세서리", 1);

    companion object {
        fun findByCategory(category: String): Category {
            return entries.firstOrNull { it.categoryName == category } ?: throw CategoryNotFoundException(category)
        }

        fun getSortedValues(): List<Category> {
            return entries.sortedByDescending { it.priority }
        }
    }
}
