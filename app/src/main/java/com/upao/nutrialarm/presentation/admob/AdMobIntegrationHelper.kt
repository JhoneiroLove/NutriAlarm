package com.upao.nutrialarm.presentation.admob

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.upao.nutrialarm.data.remote.admob.AdMobService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobIntegrationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val adMobService: AdMobService
) {

    companion object {
        private const val TAG = "AdMobIntegrationHelper"

        // Configuración de cuándo mostrar anuncios
        private const val INTERSTITIAL_ACTION_COUNT = 3 // Mostrar cada 3 acciones
        private const val REWARDED_AD_IRON_BONUS = 2.0 // Bonus de hierro por ver anuncio
    }

    private var actionCount = 0
    private var lastInterstitialTime = 0L
    private val minInterstitialInterval = 60_000L // 1 minuto mínimo entre intersticiales

    /**
     * Intenta mostrar un anuncio intersticial basado en las acciones del usuario
     */
    fun tryShowInterstitialAd(
        activity: Activity,
        onAdClosed: (() -> Unit)? = null
    ) {
        actionCount++
        val currentTime = System.currentTimeMillis()

        if (actionCount >= INTERSTITIAL_ACTION_COUNT &&
            currentTime - lastInterstitialTime >= minInterstitialInterval) {

            if (adMobService.isInterstitialAdReady()) {
                Log.d(TAG, "Showing interstitial ad after $actionCount actions")
                adMobService.showInterstitialAd(activity) {
                    actionCount = 0
                    lastInterstitialTime = currentTime
                    onAdClosed?.invoke()
                }
            } else {
                Log.d(TAG, "Interstitial ad not ready, resetting counter")
                actionCount = 0
            }
        }
    }

    /**
     * Muestra anuncio con recompensa para obtener bonus de hierro
     */
    fun showRewardedAdForIronBonus(
        activity: Activity,
        onRewardEarned: ((Double) -> Unit)? = null,
        onAdClosed: (() -> Unit)? = null
    ) {
        if (adMobService.isRewardedAdReady()) {
            Log.d(TAG, "Showing rewarded ad for iron bonus")
            adMobService.showRewardedAd(
                activity = activity,
                onUserEarnedReward = { rewardType, rewardAmount ->
                    Log.d(TAG, "User earned reward: $rewardType x $rewardAmount")
                    onRewardEarned?.invoke(REWARDED_AD_IRON_BONUS)
                },
                onAdClosed = onAdClosed
            )
        } else {
            Log.w(TAG, "Rewarded ad not ready")
            onAdClosed?.invoke()
        }
    }

    /**
     * Verifica si hay anuncios con recompensa disponibles
     */
    fun isRewardedAdAvailable(): Boolean {
        return adMobService.isRewardedAdReady()
    }

    /**
     * Fuerza la recarga de todos los anuncios
     */
    fun reloadAllAds() {
        Log.d(TAG, "Reloading all ads")
        adMobService.reloadAds()
    }

    /**
     * Obtiene el servicio AdMob para uso directo
     */
    fun getAdMobService(): AdMobService {
        return adMobService
    }
}

/**
 * Composable hook para usar AdMob en Compose
 */
@Composable
fun rememberAdMobHelper(): AdMobIntegrationHelper? {
    val context = LocalContext.current
    return remember {
        try {
            val adMobService = AdMobService(context.applicationContext)
            AdMobIntegrationHelper(context.applicationContext, adMobService)
        } catch (e: Exception) {
            Log.e("AdMobHelper", "Error creating AdMob helper", e)
            null
        }
    }
}