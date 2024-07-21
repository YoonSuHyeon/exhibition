package org.musinsa.exhibition.infrastructure.utils

import java.text.DecimalFormat

object PriceFormatUtils {
    private val decimalFormat = DecimalFormat("#,###")

    fun format(price: Long): String {
        return decimalFormat.format(price)
    }
}

