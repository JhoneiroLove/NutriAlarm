package com.upao.nutrialarm.util

import android.content.Context
import android.util.Log
import com.upao.nutrialarm.domain.model.MealType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestNotificationHelper @Inject constructor(
    private val notificationHelper: NotificationHelper
) {

    companion object {
        private const val TAG = "TestNotificationHelper"
    }

    /**
     * Envía una notificación de prueba inmediatamente
     * Útil para verificar que el sistema de notificaciones funciona
     */
    fun sendTestNotification() {
        Log.d(TAG, "Enviando notificación de prueba...")

        notificationHelper.showMealReminderNotification(
            notificationId = 9999,
            mealType = MealType.LUNCH,
            mealName = "¡Prueba de notificación!"
        )

        Log.d(TAG, "Notificación de prueba enviada")
    }

    /**
     * Envía notificaciones de prueba para todos los tipos de comida
     * Útil para probar todos los formatos de notificación
     */
    fun sendTestNotificationForAllMeals() {
        Log.d(TAG, "Enviando notificaciones de prueba para todas las comidas...")

        MealType.values().forEachIndexed { index, mealType ->
            // Retraso pequeño entre notificaciones para evitar spam
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                notificationHelper.showMealReminderNotification(
                    notificationId = 1000 + index,
                    mealType = mealType,
                    mealName = "Notificación de prueba - ${getMealName(mealType)}"
                )
                Log.d(TAG, "Notificación enviada para: ${getMealName(mealType)}")
            }, (index * 2000).toLong()) // 2 segundos entre cada una
        }
    }

    /**
     * Simula una alarma de comida específica
     * Útil para probar el flujo completo de alarmas
     */
    fun simulateMealAlarm(mealType: MealType) {
        Log.d(TAG, "Simulando alarma para: ${getMealName(mealType)}")

        notificationHelper.showMealReminderNotification(
            notificationId = mealType.ordinal + 2000,
            mealType = mealType,
            mealName = null // Usar mensaje por defecto
        )
    }

    /**
     * Prueba de notificación programada (simula alarma real)
     */
    fun scheduleTestNotification(delaySeconds: Int = 10) {
        Log.d(TAG, "Programando notificación de prueba en $delaySeconds segundos...")

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            notificationHelper.showMealReminderNotification(
                notificationId = 5555,
                mealType = MealType.AFTERNOON_SNACK,
                mealName = "¡Alarma de prueba funcionando! ⏰"
            )
            Log.d(TAG, "Notificación programada ejecutada")
        }, (delaySeconds * 1000).toLong())

        Log.d(TAG, "Notificación programada para $delaySeconds segundos")
    }

    private fun getMealName(mealType: MealType): String {
        return when (mealType) {
            MealType.BREAKFAST -> "Desayuno"
            MealType.SCHOOL_SNACK -> "Refrigerio Escolar"
            MealType.LUNCH -> "Almuerzo"
            MealType.AFTERNOON_SNACK -> "Merienda de Tarde"
            MealType.DINNER -> "Cena"
            MealType.OPTIONAL_SNACK -> "Snack Opcional"
        }
    }
}