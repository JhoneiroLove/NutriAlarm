package com.upao.nutrialarm.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatUtils {

    private val decimalFormat = DecimalFormat("#.#")
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun formatNutrientValue(value: Double, unit: String = ""): String {
        val formatted = decimalFormat.format(value)
        return if (unit.isNotEmpty()) "$formatted $unit" else formatted
    }

    fun formatCalories(calories: Double): String {
        return "${calories.toInt()} cal"
    }

    fun formatIron(iron: Double): String {
        return formatNutrientValue(iron, "mg")
    }

    fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return timeFormat.format(calendar.time)
    }

    fun formatTime(timeString: String): String {
        return try {
            val parts = timeString.split(":")
            if (parts.size >= 2) {
                formatTime(parts[0].toInt(), parts[1].toInt())
            } else {
                timeString
            }
        } catch (e: Exception) {
            timeString
        }
    }

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    fun formatBMI(bmi: Double): String {
        return decimalFormat.format(bmi)
    }

    fun formatPercentage(value: Double, total: Double): String {
        val percentage = if (total > 0) (value / total) * 100 else 0.0
        return "${percentage.toInt()}%"
    }

    fun formatDuration(minutes: Int): String {
        return when {
            minutes < 60 -> "${minutes} min"
            minutes == 60 -> "1 hora"
            minutes < 120 -> "1 hora ${minutes - 60} min"
            else -> {
                val hours = minutes / 60
                val remainingMinutes = minutes % 60
                if (remainingMinutes == 0) {
                    "$hours horas"
                } else {
                    "$hours horas $remainingMinutes min"
                }
            }
        }
    }
}