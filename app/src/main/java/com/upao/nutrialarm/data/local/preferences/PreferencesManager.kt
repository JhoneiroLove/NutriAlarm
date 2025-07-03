package com.upao.nutrialarm.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREF_NAME = "nutrialarm_preferences"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_USER_ONBOARDED = "user_onboarded"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_REMINDER_SOUND = "reminder_sound"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_LAST_SYNC = "last_sync"
        private const val KEY_CURRENT_USER_ID = "current_user_id"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Primera vez que se abre la app
    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    // Usuario completó el onboarding
    var isUserOnboarded: Boolean
        get() = prefs.getBoolean(KEY_USER_ONBOARDED, false)
        set(value) = prefs.edit().putBoolean(KEY_USER_ONBOARDED, value).apply()

    // Configuraciones de notificaciones
    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, value).apply()

    // Usuario actual
    var currentUserId: String?
        get() = prefs.getString(KEY_CURRENT_USER_ID, null)
        set(value) = prefs.edit().putString(KEY_CURRENT_USER_ID, value).apply()

    // Última sincronización
    var lastSyncTimestamp: Long
        get() = prefs.getLong(KEY_LAST_SYNC, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_SYNC, value).apply()

    // Modo de tema
    enum class ThemeMode { LIGHT, DARK, SYSTEM }

    var themeMode: ThemeMode
        get() {
            val mode = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
            return try {
                ThemeMode.valueOf(mode!!)
            } catch (e: Exception) {
                ThemeMode.SYSTEM
            }
        }
        set(value) = prefs.edit().putString(KEY_THEME_MODE, value.name).apply()

    // Limpiar todas las preferencias (logout)
    fun clearAllPreferences() {
        prefs.edit().clear().apply()
    }

    // Limpiar solo datos de usuario (mantener configuraciones)
    fun clearUserData() {
        prefs.edit()
            .remove(KEY_CURRENT_USER_ID)
            .remove(KEY_USER_ONBOARDED)
            .remove(KEY_LAST_SYNC)
            .apply()
    }

    // Exportar configuraciones para backup
    fun exportSettings(): Map<String, Any?> {
        return mapOf(
            "notifications_enabled" to notificationsEnabled,
            "vibration_enabled" to vibrationEnabled,
            "theme_mode" to themeMode.name
        )
    }

    // Importar configuraciones desde backup
    fun importSettings(settings: Map<String, Any?>) {
        prefs.edit().apply {
            settings["notifications_enabled"]?.let {
                putBoolean(KEY_NOTIFICATIONS_ENABLED, it as Boolean)
            }
            settings["vibration_enabled"]?.let {
                putBoolean(KEY_VIBRATION_ENABLED, it as Boolean)
            }
            settings["theme_mode"]?.let {
                putString(KEY_THEME_MODE, it as String)
            }
        }.apply()
    }
}