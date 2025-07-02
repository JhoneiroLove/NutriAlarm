package com.upao.nutrialarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class MealConsumedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val notificationId = intent.getIntExtra("notification_id", -1)
        if (notificationId != -1) {
            // Cancelar la notificación
            NotificationManagerCompat.from(context).cancel(notificationId)

            // Aquí podrías agregar lógica para marcar la comida como consumida
            // en la base de datos si implementas ese tracking
        }
    }
}