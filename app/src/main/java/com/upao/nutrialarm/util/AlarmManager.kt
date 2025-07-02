package com.upao.nutrialarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.upao.nutrialarm.domain.model.Alarm
import com.upao.nutrialarm.domain.model.DayOfWeek
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmManagerUtil @Inject constructor(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(alarm: Alarm) {
        if (alarm.days.isEmpty()) {
            // Alarma de una sola vez
            scheduleOneTimeAlarm(alarm)
        } else {
            // Alarma recurrente (para cada día seleccionado)
            alarm.days.forEach { dayOfWeek ->
                scheduleRecurringAlarm(alarm, dayOfWeek)
            }
        }
    }

    private fun scheduleOneTimeAlarm(alarm: Alarm) {
        val calendar = getCalendarForTime(alarm.time)
        val intent = createAlarmIntent(alarm)
        val pendingIntent = createPendingIntent(alarm.id.hashCode(), intent)

        if (canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    private fun scheduleRecurringAlarm(alarm: Alarm, dayOfWeek: DayOfWeek) {
        val calendar = getCalendarForTimeAndDay(alarm.time, dayOfWeek)
        val intent = createAlarmIntent(alarm)
        val requestCode = "${alarm.id}_${dayOfWeek.name}".hashCode()
        val pendingIntent = createPendingIntent(requestCode, intent)

        if (canScheduleExactAlarms()) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7, // Repetir cada semana
                pendingIntent
            )
        } else {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(alarm: Alarm) {
        if (alarm.days.isEmpty()) {
            // Cancelar alarma de una vez
            val intent = createAlarmIntent(alarm)
            val pendingIntent = createPendingIntent(alarm.id.hashCode(), intent)
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        } else {
            // Cancelar alarmas recurrentes
            alarm.days.forEach { dayOfWeek ->
                val intent = createAlarmIntent(alarm)
                val requestCode = "${alarm.id}_${dayOfWeek.name}".hashCode()
                val pendingIntent = createPendingIntent(requestCode, intent)
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    private fun createAlarmIntent(alarm: Alarm): Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id)
            putExtra("meal_type", alarm.mealType.name)
            putExtra("reminder_message", alarm.reminderMessage)
        }
    }

    private fun createPendingIntent(requestCode: Int, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getCalendarForTime(time: String): Calendar {
        val calendar = Calendar.getInstance()
        val timeParts = time.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Si la hora ya pasó hoy, programar para mañana
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendar
    }

    private fun getCalendarForTimeAndDay(time: String, dayOfWeek: DayOfWeek): Calendar {
        val calendar = Calendar.getInstance()
        val timeParts = time.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Mapear nuestro enum al Calendar
        val calendarDayOfWeek = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> Calendar.SUNDAY
            DayOfWeek.MONDAY -> Calendar.MONDAY
            DayOfWeek.TUESDAY -> Calendar.TUESDAY
            DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
            DayOfWeek.THURSDAY -> Calendar.THURSDAY
            DayOfWeek.FRIDAY -> Calendar.FRIDAY
            DayOfWeek.SATURDAY -> Calendar.SATURDAY
        }

        // Ajustar al próximo día de la semana especificado
        while (calendar.get(Calendar.DAY_OF_WEEK) != calendarDayOfWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Si la hora ya pasó hoy y es el mismo día, mover a la próxima semana
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        return calendar
    }

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}