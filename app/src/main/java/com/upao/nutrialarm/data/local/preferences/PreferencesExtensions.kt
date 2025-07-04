package com.upao.nutrialarm.data.local.preferences

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

// Constantes para las preferencias de AdMob
private const val PREF_DAILY_IRON_BONUS_PREFIX = "daily_iron_bonus_"
private const val PREF_LAST_BONUS_DATE = "last_bonus_date"
private const val PREFS_NAME = "nutrialarm_preferences"
private const val TAG = "PreferencesExtensions"

/**
 * Función auxiliar para obtener SharedPreferences usando reflection de manera segura
 */
private fun PreferencesManager.getSharedPrefs(): android.content.SharedPreferences? {
    return try {
        val contextField = this::class.java.getDeclaredField("context")
        contextField.isAccessible = true
        val context = contextField.get(this) as Context
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    } catch (e: Exception) {
        Log.e(TAG, "Error accessing SharedPreferences", e)
        null
    }
}

/**
 * Guarda el bonus de hierro diario
 */
fun PreferencesManager.saveDailyIronBonusAmount(bonusAmount: Double) {
    try {
        val today = getCurrentDateString()
        val prefs = getSharedPrefs()
        val key = "$PREF_DAILY_IRON_BONUS_PREFIX$today"

        // Limpiar valor anterior si existe para evitar conflictos de tipo
        prefs?.edit()?.remove(key)?.apply()

        // Guardar como String para evitar problemas de cast
        prefs?.edit()
            ?.putString(key, bonusAmount.toString())
            ?.putString(PREF_LAST_BONUS_DATE, today)
            ?.apply()

        Log.d(TAG, "Saved daily iron bonus: ${bonusAmount}mg for date: $today")
    } catch (e: Exception) {
        Log.e(TAG, "Error saving daily iron bonus", e)
    }
}

/**
 * Obtiene el bonus de hierro para el día actual - VERSIÓN ROBUSTA
 */
fun PreferencesManager.getDailyIronBonusAmount(): Double {
    return try {
        val today = getCurrentDateString()
        val prefs = getSharedPrefs()
        val key = "$PREF_DAILY_IRON_BONUS_PREFIX$today"

        // Intentar leer como String primero
        val bonusFromString = try {
            val bonusString = prefs?.getString(key, null)
            bonusString?.toDoubleOrNull()
        } catch (e: ClassCastException) {
            Log.w(TAG, "Found Float value, converting to Double")
            null
        }

        // Si no funciona como String, intentar como Float (datos antiguos)
        val bonusFromFloat = if (bonusFromString == null) {
            try {
                val bonusFloat = prefs?.getFloat(key, 0f)
                bonusFloat?.toDouble()
            } catch (e: ClassCastException) {
                Log.w(TAG, "Cannot read as Float either, using default")
                null
            }
        } else null

        val bonus = bonusFromString ?: bonusFromFloat ?: 0.0

        // Si encontramos un valor Float, convertirlo a String para futuros usos
        if (bonusFromString == null && bonusFromFloat != null && bonusFromFloat > 0) {
            Log.d(TAG, "Converting old Float value to String format")
            prefs?.edit()?.remove(key)?.apply() // Limpiar Float
            prefs?.edit()?.putString(key, bonus.toString())?.apply() // Guardar como String
        }

        Log.d(TAG, "Retrieved daily iron bonus: ${bonus}mg for date: $today")
        bonus
    } catch (e: Exception) {
        Log.e(TAG, "Error retrieving daily iron bonus", e)
        0.0
    }
}

/**
 * Limpia los bonus de hierro de días anteriores
 */
fun PreferencesManager.cleanupOldIronBonusesData() {
    try {
        val prefs = getSharedPrefs()
        val allPrefs = prefs?.all
        val today = getCurrentDateString()
        val editor = prefs?.edit()

        var removedCount = 0
        allPrefs?.keys?.forEach { key ->
            if (key.startsWith(PREF_DAILY_IRON_BONUS_PREFIX) && !key.endsWith(today)) {
                editor?.remove(key)
                removedCount++
            }
        }

        editor?.apply()
        Log.d(TAG, "Cleaned up $removedCount old iron bonus entries")
    } catch (e: Exception) {
        Log.e(TAG, "Error cleaning up old iron bonuses", e)
    }
}

/**
 * Resetea el bonus de hierro diario
 */
fun PreferencesManager.resetDailyIronBonusAmount() {
    try {
        val today = getCurrentDateString()
        val prefs = getSharedPrefs()
        val key = "$PREF_DAILY_IRON_BONUS_PREFIX$today"

        prefs?.edit()?.remove(key)?.apply()

        Log.d(TAG, "Reset daily iron bonus for date: $today")
    } catch (e: Exception) {
        Log.e(TAG, "Error resetting daily iron bonus", e)
    }
}

/**
 * Verifica si es un nuevo día desde el último bonus
 */
fun PreferencesManager.isNewDayForBonus(): Boolean {
    return try {
        val today = getCurrentDateString()
        val prefs = getSharedPrefs()
        val lastBonusDate = prefs?.getString(PREF_LAST_BONUS_DATE, "")
        today != lastBonusDate
    } catch (e: Exception) {
        Log.e(TAG, "Error checking if new day", e)
        true
    }
}

fun PreferencesManager.cleanupCorruptedIronBonusData() {
    try {
        val prefs = getSharedPrefs()
        val editor = prefs?.edit()
        val allPrefs = prefs?.all

        var cleanedCount = 0

        // Limpiar todas las entradas de iron bonus corruptas
        allPrefs?.keys?.filter { it.startsWith(PREF_DAILY_IRON_BONUS_PREFIX) }?.forEach { key ->
            try {
                // Intentar leer como String
                prefs.getString(key, null)
            } catch (e: ClassCastException) {
                // Si falla, eliminar la entrada corrupta
                editor?.remove(key)
                cleanedCount++
                Log.w(TAG, "Removed corrupted iron bonus entry: $key")
            }
        }

        editor?.apply()
        Log.i(TAG, "Cleaned up $cleanedCount corrupted iron bonus entries")
    } catch (e: Exception) {
        Log.e(TAG, "Error during corruption cleanup", e)
    }
}

/**
 * Obtiene la fecha actual como string en formato yyyy-MM-dd
 */
private fun getCurrentDateString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(Date())
}

/**
 * Obtiene el bonus de hierro para una fecha específica
 */
fun PreferencesManager.getDailyIronBonusForDate(date: String): Double {
    return try {
        val prefs = getSharedPrefs()
        val key = "$PREF_DAILY_IRON_BONUS_PREFIX$date"

        // Usar la misma lógica robusta que getDailyIronBonusAmount
        val bonusFromString = try {
            val bonusString = prefs?.getString(key, null)
            bonusString?.toDoubleOrNull()
        } catch (e: ClassCastException) {
            null
        }

        val bonusFromFloat = if (bonusFromString == null) {
            try {
                val bonusFloat = prefs?.getFloat(key, 0f)
                bonusFloat?.toDouble()
            } catch (e: ClassCastException) {
                null
            }
        } else null

        val bonus = bonusFromString ?: bonusFromFloat ?: 0.0

        Log.d(TAG, "Retrieved daily iron bonus: ${bonus}mg for date: $date")
        bonus
    } catch (e: Exception) {
        Log.e(TAG, "Error retrieving daily iron bonus for date: $date", e)
        0.0
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
            totalBonus += getDailyIronBonusForDate(date)
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
 * Data class para estadísticas del bonus de hierro
 */
data class IronBonusStats(
    val todayBonus: Double,
    val weeklyTotal: Double,
    val averageDaily: Double,
    val daysWithBonus: Int
)

/**
 * Obtiene estadísticas del bonus de hierro
 */
fun PreferencesManager.getIronBonusStats(): IronBonusStats {
    return try {
        val todayBonus = getDailyIronBonusAmount()
        val weeklyTotal = getWeeklyIronBonusTotal()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var daysWithBonus = 0

        repeat(7) { daysBack ->
            val date = dateFormat.format(calendar.time)
            if (getDailyIronBonusForDate(date) > 0) {
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