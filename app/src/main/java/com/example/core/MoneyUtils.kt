package com.example.core

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Utility for exact financial calculations using BigDecimal to prevent any floating-point inaccuracies.
 */
object MoneyUtils {
    val ZERO: BigDecimal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)

    private val arabicDecimalFormat: DecimalFormat by lazy {
        val symbols = DecimalFormatSymbols(Locale("ar")).apply {
            groupingSeparator = ','
            decimalSeparator = '.'
        }
        DecimalFormat("#,##0.00", symbols)
    }

    private val englishDecimalFormat: DecimalFormat by lazy {
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = ','
            decimalSeparator = '.'
        }
        DecimalFormat("#,##0.00", symbols)
    }

    fun parseBigDecimal(raw: String?): BigDecimal? {
        if (raw.isNullOrBlank()) return null
        return try {
            val cleaned = raw.trim()
                .replace("،", ".")
                .replace(",", ".")
                .replace(" ", "")
                // Remove any Arabic numeral conversion if entered
                .map { char ->
                    when (char) {
                        '٠' -> '0'; '١' -> '1'; '٢' -> '2'; '٣' -> '3'; '٤' -> '4'
                        '٥' -> '5'; '٦' -> '6'; '٧' -> '7'; '٨' -> '8'; '٩' -> '9'
                        else -> char
                    }
                }.joinToString("")
            val value = BigDecimal(cleaned).setScale(2, RoundingMode.HALF_UP)
            if (value > BigDecimal.ZERO) value else null
        } catch (_: Exception) {
            null
        }
    }

    fun parseBigDecimalAllowZero(raw: String?): BigDecimal {
        return parseBigDecimal(raw) ?: ZERO
    }

    fun format(amount: BigDecimal?, currency: String = "ج.م"): String {
        val value = amount ?: BigDecimal.ZERO
        val formatted = englishDecimalFormat.format(value)
        return "$formatted $currency"
    }

    fun formatPlain(amount: BigDecimal?): String {
        val value = amount ?: BigDecimal.ZERO
        return englishDecimalFormat.format(value)
    }

    fun add(a: BigDecimal, b: BigDecimal): BigDecimal {
        return a.add(b).setScale(2, RoundingMode.HALF_UP)
    }

    fun subtract(a: BigDecimal, b: BigDecimal): BigDecimal {
        return a.subtract(b).setScale(2, RoundingMode.HALF_UP)
    }
}
