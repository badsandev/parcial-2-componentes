package com.taller.parcial2.utils

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {

    // Formatea un número como moneda colombiana
    fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        return format.format(amount)
    }

    // Formatea el porcentaje de progreso
    fun formatPercentage(value: Double): String {
        return "%.1f%%".format(value)
    }

    // Formatea una fecha ISO a formato legible
    fun formatDate(isoDate: String?): String {
        if (isoDate == null) return ""
        return try {
            val parts = isoDate.split("-")
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } catch (e: Exception) {
            isoDate
        }
    }

    // Hoy en formato YYYY-MM-DD
    fun today(): String {
        val cal = java.util.Calendar.getInstance()
        return "%04d-%02d-%02d".format(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH) + 1,
            cal.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }
}
