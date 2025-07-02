package com.upao.nutrialarm.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.upao.nutrialarm.MainActivity
import com.upao.nutrialarm.R
import com.upao.nutrialarm.domain.model.MealType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    private val context: Context
) {

    companion object {
        const val MEAL_REMINDER_CHANNEL_ID = "meal_reminder_channel"
        const val NUTRITION_TIPS_CHANNEL_ID = "nutrition_tips_channel"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val mealReminderChannel = NotificationChannel(
                MEAL_REMINDER_CHANNEL_ID,
                "Recordatorios de Comida",
                NotificationManager.IMPORTANCE_HIGH
            )

            val nutritionTipsChannel = NotificationChannel(
                NUTRITION_TIPS_CHANNEL_ID,
                "Consejos Nutricionales",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(mealReminderChannel)
            notificationManager.createNotificationChannel(nutritionTipsChannel)
        }
    }

    fun showMealReminderNotification(
        notificationId: Int,
        mealType: MealType,
        mealName: String? = null
    ) {
        // Verificar permiso antes de mostrar notificación
        if (!hasNotificationPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val (title, content) = getMealNotificationContent(mealType, mealName)

        val notification = NotificationCompat.Builder(context, MEAL_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Permission denied, handle silently
        }
    }

    private fun getMealNotificationContent(mealType: MealType, mealName: String?): Pair<String, String> {
        return when (mealType) {
            MealType.BREAKFAST -> Pair("🌅 ¡Hora del desayuno!", "Recuerda incluir alimentos ricos en hierro")
            MealType.SCHOOL_SNACK -> Pair("🍎 Refrigerio escolar", "Un snack saludable te ayudará")
            MealType.LUNCH -> Pair("🍽️ ¡Hora del almuerzo!", "Recarga energías con una comida balanceada")
            MealType.AFTERNOON_SNACK -> Pair("🥜 Merienda de tarde", "Mantén estables tus niveles de energía")
            MealType.DINNER -> Pair("🌙 ¡Hora de cenar!", "Una cena nutritiva para descansar mejor")
            MealType.OPTIONAL_SNACK -> Pair("☕ Snack opcional", "¿Un pequeño snack? Solo si lo necesitas")
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
}