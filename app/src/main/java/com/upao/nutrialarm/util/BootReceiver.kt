package com.upao.nutrialarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmManagerUtil: AlarmManagerUtil

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reestablecer todas las alarmas después del reinicio
            CoroutineScope(Dispatchers.IO).launch {
                // Aquí necesitarías obtener todas las alarmas activas de la BD
                // y reprogramarlas usando alarmManagerUtil.scheduleAlarm()
                // Por ahora dejamos la estructura preparada
            }
        }
    }
}