package com.upao.nutrialarm.data.local.preferences

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extensiones para PreferencesManager para manejar el bonus de hierro diario
 */

private const val KEY_IRON_BONUS_PREFIX = "daily_iron_bonus"
private const val TAG = "PreferencesManagerExt"

/**
 * Guarda el bonus de hierro para el día actual
 */
fun PreferencesManager.saveDailyIronBonus(bonus: Double) {
    try {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val key = "${KEY_IRON_BONUS_PREFIX}_$today"

        // Usando el contexto interno para acceder a SharedPreferences directamente
        val prefs = javaClass.getDeclaredField("prefs").apply { isAccessible = true }.get(this) as android.content.SharedPreferences
        prefs.edit().putFloat(key, bonus.toFloat()).apply()

        Log.d(TAG, "Saved daily iron bonus: ${bonus}mg for date: $today")
    } catch (e: Exception) {
        Log.e(TAG, "Error saving daily iron bonus", e)
    }
}

/**
 * Obtiene el bonus de hierro para el día actual
 */
fun PreferencesManager.getDailyIronBonus(): Double {
    return try {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val key = "${KEY_IRON_BONUS_PREFIX}_$today"

        // Usando reflexión para acceder a SharedPreferences
        val prefs = javaClass.getDeclaredField("prefs").apply { isAccessible = true }.get(this) as android.content.SharedPreferences
        val bonus = prefs.getFloat(key, 0f).toDouble()

        Log.d(TAG, "Retrieved daily iron bonus: ${bonus}mg for date: $today")
        bonus
    } catch (e: Exception) {
        Log.e(TAG, "Error retrieving daily iron bonus", e)
        0.0
    }
}

/**
 * Obtiene el bonus de hierro para una fecha específica
 */
fun PreferencesManager.getDailyIronBonus(date: String): Double {
    return try {
        val key = "${KEY_IRON_BONUS_PREFIX}_$date"

        val prefs = javaClass.getDeclaredField("prefs").apply { isAccessible = true }.get(this) as android.content.SharedPreferences
        val bonus = prefs.getFloat(key, 0f).toDouble()

        Log.d(TAG, "Retrieved daily iron bonus: ${bonus}mg for date: $date")
        bonus
    } catch (e: Exception) {
        Log.e(TAG, "Error retrieving daily iron bonus for date: $date", e)
        0.0
    }
}

/**
 * Limpia el bonus de hierro para el día actual
 */
fun PreferencesManager.clearDailyIronBonus() {
    try {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val key = "${KEY_IRON_BONUS_PREFIX}_$today"

        val prefs = javaClass.getDeclaredField("prefs").apply { isAccessible = true }.get(this) as android.content.SharedPreferences
        prefs.edit().remove(key).apply()

        Log.d(TAG, "Cleared daily iron bonus for date: $today")
    } catch (e: Exception) {
        Log.e(TAG, "Error clearing daily iron bonus", e)
    }
}

/**
 * Limpia todos los bonus de hierro antiguos (más de 30 días)
 */
fun PreferencesManager.cleanupOldIronBonuses() {
    try {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30) // 30 días atrás
        val cutoffDate = calendar.time

        val prefs = javaClass.getDeclaredField("prefs").apply { isAccessible = true }.get(this) as android.content.SharedPreferences
        val editor = prefs.edit()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var removedCount = 0

        prefs.all.keys.filter { it.startsWith(KEY_IRON_BONUS_PREFIX) }.forEach { key ->
            try {
                val dateString = key.substring("${KEY_IRON_BONUS_PREFIX}_".length)
                val date = dateFormat.parse(dateString)

                if (date != null && date.before(cutoffDate)) {
                    editor.remove(key)
                    removedCount++
                }
            } catch (e: Exception) {
                // Si no se puede parsear la fecha, eliminar la clave por seguridad
                editor.remove(key)
                removedCount++
            }
        }

        editor.apply()
        Log.d(TAG, "Cleaned up $removedCount old iron bonus entries")
    } catch (e: Exception) {
        Log.e(TAG, "Error cleaning up old iron bonuses", e)
    }
}

/**
 * Obtiene el total de bonus de hierro de los últimos 7 días
 */
fun PreferencesManager.getWeeklyIronBonusTotal(): Double {
    return try {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var totalBonus = 0.0

        repeat(7) { daysBack ->
            val date = dateFormat.format(calendar.time)
            totalBonus += getDailyIronBonus(date)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        Log.d(TAG, "Weekly iron bonus total: ${totalBonus}mg")
        totalBonus
    } catch (e: Exception) {
        Log.e(TAG, "Error calculating weekly iron bonus", e)
        0.0
    }
}

/**
 * Obtiene estadísticas del bonus de hierro
 */
data class IronBonusStats(
    val todayBonus: Double,
    val weeklyTotal: Double,
    val averageDaily: Double,
    val daysWithBonus: Int
)

fun PreferencesManager.getIronBonusStats(): IronBonusStats {
    return try {
        val todayBonus = getDailyIronBonus()
        val weeklyTotal = getWeeklyIronBonusTotal()

        // Calcular días con bonus en la última semana
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var daysWithBonus = 0

        repeat(7) { daysBack ->
            val date = dateFormat.format(calendar.time)
            if (getDailyIronBonus(date) > 0) {
                daysWithBonus++
            }
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        val averageDaily = if (daysWithBonus > 0) weeklyTotal / daysWithBonus else 0.0

        IronBonusStats(
            todayBonus = todayBonus,
            weeklyTotal = weeklyTotal,
            averageDaily = averageDaily,
            daysWithBonus = daysWithBonus
        )
    } catch (e: Exception) {
        Log.e(TAG, "Error calculating iron bonus stats", e)
        IronBonusStats(0.0, 0.0, 0.0, 0)
    }
}