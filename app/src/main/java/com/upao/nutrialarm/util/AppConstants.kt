package com.upao.nutrialarm.util

object AppConstants {

    // Información de la app
    const val APP_NAME = "NutriAlarm"
    const val APP_VERSION = "1.0.0"
    const val PRIVACY_POLICY_URL = "https://nutrialarm.com/privacy"
    const val TERMS_OF_SERVICE_URL = "https://nutrialarm.com/terms"

    // Configuraciones de base de datos
    const val DATABASE_NAME = "nutrialarm_database"
    const val DATABASE_VERSION = 2

    // Configuraciones de notificaciones
    const val NOTIFICATION_CHANNEL_ID = "meal_reminders"
    const val NOTIFICATION_CHANNEL_NAME = "Recordatorios de Comida"

    // Configuraciones de alarmas
    const val ALARM_REQUEST_CODE_BASE = 1000
    const val MAX_ALARMS_PER_USER = 50

    // Valores nutricionales
    const val DAILY_IRON_RECOMMENDATION_LOW = 8.0 // mg
    const val DAILY_IRON_RECOMMENDATION_MEDIUM = 12.0 // mg
    const val DAILY_IRON_RECOMMENDATION_HIGH = 18.0 // mg

    const val DAILY_CALORIES_MIN = 1200.0
    const val DAILY_CALORIES_MAX = 3000.0

    // Configuraciones de UI
    const val ANIMATION_DURATION_SHORT = 300
    const val ANIMATION_DURATION_MEDIUM = 600
    const val ANIMATION_DURATION_LONG = 1000

    const val MESSAGE_DISPLAY_DURATION = 3000L // milliseconds

    // Configuraciones de red
    const val NETWORK_TIMEOUT = 30_000L // milliseconds
    const val RETRY_ATTEMPTS = 3

    // Configuraciones de archivos
    const val MAX_FILE_SIZE_MB = 5
    const val ALLOWED_IMAGE_FORMATS = "jpg,jpeg,png,webp"

    // Configuraciones de usuario
    const val MIN_AGE = 5
    const val MAX_AGE = 100
    const val MIN_WEIGHT = 20.0 // kg
    const val MAX_WEIGHT = 300.0 // kg
    const val MIN_HEIGHT = 50.0 // cm
    const val MAX_HEIGHT = 250.0 // cm

    // IDs de AdMob
    object AdMob {
        const val APP_ID = "ca-app-pub-9430651300419058~2826649282"
        const val BANNER_AD_UNIT_ID = "ca-app-pub-9430651300419058/3671428294"
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9430651300419058/4409794894"
        const val REWARDED_AD_UNIT_ID = "ca-app-pub-9430651300419058/2240325924"

        // IDs de prueba
        const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}