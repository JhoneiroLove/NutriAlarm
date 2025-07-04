package com.upao.nutrialarm.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_AUTO_LOGIN = "auto_login"
        private const val TAG = "PreferencesManager"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var isUserLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) {
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
            Log.d(TAG, "Usuario logueado: $value")
        }

    var currentUserId: String?
        get() = prefs.getString(KEY_CURRENT_USER_ID, null)
        set(value) {
            prefs.edit().putString(KEY_CURRENT_USER_ID, value).apply()
            Log.d(TAG, "ID de usuario actual guardado: $value")
        }

    var userEmail: String?
        get() = prefs.getString(KEY_USER_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var userName: String?
        get() = prefs.getString(KEY_USER_NAME, null)
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var autoLoginEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_LOGIN, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_LOGIN, value).apply()

    fun saveUserSession(userId: String, email: String, name: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_CURRENT_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putLong(KEY_LAST_SYNC, System.currentTimeMillis())
            apply()
        }
        Log.d(TAG, "Sesión guardada para usuario: $userId ($email)")
    }

    fun clearUserSession() {
        val editor = prefs.edit()
        editor.remove(KEY_IS_LOGGED_IN)
        editor.remove(KEY_CURRENT_USER_ID)
        editor.remove(KEY_USER_EMAIL)
        editor.remove(KEY_USER_NAME)
        editor.apply()
        Log.d(TAG, "Sesión de usuario limpiada")
    }

    fun hasValidSession(): Boolean {
        val hasSession = isUserLoggedIn && !currentUserId.isNullOrEmpty()
        Log.d(TAG, "Verificando sesión válida: $hasSession")
        return hasSession
    }

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    var isUserOnboarded: Boolean
        get() = prefs.getBoolean(KEY_USER_ONBOARDED, false)
        set(value) = prefs.edit().putBoolean(KEY_USER_ONBOARDED, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, value).apply()

    var lastSyncTimestamp: Long
        get() = prefs.getLong(KEY_LAST_SYNC, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_SYNC, value).apply()

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

    fun clearAllPreferences() {
        prefs.edit().clear().apply()
        Log.d(TAG, "Todas las preferencias limpiadas")
    }

    fun clearUserData() {
        clearUserSession()
        prefs.edit().apply {
            remove(KEY_USER_ONBOARDED)
            remove(KEY_LAST_SYNC)
            apply()
        }
        Log.d(TAG, "Datos de usuario limpiados")
    }

    fun exportSettings(): Map<String, Any?> {
        return mapOf(
            "notifications_enabled" to notificationsEnabled,
            "vibration_enabled" to vibrationEnabled,
            "theme_mode" to themeMode.name,
            "auto_login" to autoLoginEnabled
        )
    }

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
            settings["auto_login"]?.let {
                putBoolean(KEY_AUTO_LOGIN, it as Boolean)
            }
        }.apply()
        Log.d(TAG, "Configuraciones importadas")
    }

    fun getSessionInfo(): String {
        return """
            Usuario logueado: $isUserLoggedIn
            ID de usuario: $currentUserId
            Email: $userEmail
            Nombre: $userName
            Auto-login: $autoLoginEnabled
            Última sincronización: $lastSyncTimestamp
        """.trimIndent()
    }
}