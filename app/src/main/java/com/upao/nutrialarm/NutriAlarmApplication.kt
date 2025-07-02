package com.upao.nutrialarm

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NutriAlarmApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    companion object {
        private const val TAG = "NutriAlarmApplication"
    }

    override fun onCreate() {
        super.onCreate()

        // Inicializar MobileAds de forma segura
        initializeMobileAds()

        // Configurar WorkManager para notificaciones
        setupWorkManager()
    }

    private fun initializeMobileAds() {
        try {
            MobileAds.initialize(this, object : OnInitializationCompleteListener {
                override fun onInitializationComplete(initializationStatus: InitializationStatus) {
                    Log.d(TAG, "AdMob initialization complete")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing AdMob: ${e.message}", e)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun setupWorkManager() {
        try {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<SyncDataWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "sync_data_work",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up WorkManager: ${e.message}", e)
        }
    }
}