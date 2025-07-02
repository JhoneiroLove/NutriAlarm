package com.upao.nutrialarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.upao.nutrialarm.domain.model.MealType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val alarmId = intent.getStringExtra("alarm_id") ?: return
        val mealTypeString = intent.getStringExtra("meal_type") ?: return
        val reminderMessage = intent.getStringExtra("reminder_message") ?: ""

        try {
            val mealType = MealType.valueOf(mealTypeString)
            val notificationId = alarmId.hashCode()

            notificationHelper.showMealReminderNotification(
                notificationId = notificationId,
                mealType = mealType,
                mealName = reminderMessage.ifEmpty { null }
            )
        } catch (e: Exception) {
            // Log error pero no crashear
            e.printStackTrace()
        }
    }
}