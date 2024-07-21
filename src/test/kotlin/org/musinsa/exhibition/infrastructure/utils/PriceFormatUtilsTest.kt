package org.musinsa.exhibition.infrastructure.utils

import org.junit.jupiter.api.Test

class PriceFormatUtilsTest {

    @Test
    fun format() {
        val wonFormat = PriceFormatUtils.format(300000)

        assert(wonFormat == "300,000")
    }
}
