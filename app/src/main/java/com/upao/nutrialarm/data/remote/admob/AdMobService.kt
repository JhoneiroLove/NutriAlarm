package com.upao.nutrialarm.data.remote.admob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobService @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val TAG = "AdMobService"

        // Tus IDs reales de AdMob
        private const val BANNER_AD_UNIT_ID = "ca-app-pub-9430651300419058/3671428294"
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9430651300419058/4409794894"
        private const val REWARDED_AD_UNIT_ID = "ca-app-pub-9430651300419058/2240325924"

        // IDs de prueba (para desarrollo/testing)
        private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

        // Flag para usar IDs de prueba o reales
        private const val USE_TEST_ADS = false // Cambiado a false para usar tus IDs reales
    }

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    init {
        loadInterstitialAd()
        loadRewardedAd()
    }

    // Banner Ads
    fun createBannerAd(): AdView {
        return AdView(context).apply {
            adUnitId = if (USE_TEST_ADS) TEST_BANNER_AD_UNIT_ID else BANNER_AD_UNIT_ID
            setAdSize(AdSize.BANNER)

            // Configurar listener para eventos del banner
            adListener = object : AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(TAG, "Banner ad clicked")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d(TAG, "Banner ad loaded successfully")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Log.e(TAG, "Banner ad failed to load: ${adError.message}")
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.d(TAG, "Banner ad opened")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.d(TAG, "Banner ad closed")
                }
            }
        }
    }

    fun loadBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        Log.d(TAG, "Loading banner ad...")
    }

    // Interstitial Ads
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        val adUnitId = if (USE_TEST_ADS) TEST_INTERSTITIAL_AD_UNIT_ID else INTERSTITIAL_AD_UNIT_ID

        Log.d(TAG, "Loading interstitial ad with ID: $adUnitId")

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e(TAG, "Interstitial ad failed to load: ${adError.message}")
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded successfully")
                    interstitialAd = ad
                    setupInterstitialCallbacks(ad)
                }
            }
        )
    }

    private fun setupInterstitialCallbacks(ad: InterstitialAd) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "Interstitial ad clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                Log.d(TAG, "Interstitial ad dismissed")
                interstitialAd = null
                loadInterstitialAd() // Precargar el siguiente
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                interstitialAd = null
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "Interstitial ad impression recorded")
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                Log.d(TAG, "Interstitial ad showed full screen content")
            }
        }
    }

    fun showInterstitialAd(activity: Activity, onAdClosed: (() -> Unit)? = null) {
        interstitialAd?.let { ad ->
            Log.d(TAG, "Showing interstitial ad")
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    interstitialAd = null
                    loadInterstitialAd()
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.e(TAG, "Failed to show interstitial ad: ${adError.message}")
                    interstitialAd = null
                    onAdClosed?.invoke()
                }
            }
            ad.show(activity)
        } ?: run {
            Log.w(TAG, "Interstitial ad not ready")
            onAdClosed?.invoke()
            loadInterstitialAd() // Intentar cargar uno nuevo
        }
    }

    // Rewarded Ads
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        val adUnitId = if (USE_TEST_ADS) TEST_REWARDED_AD_UNIT_ID else REWARDED_AD_UNIT_ID

        Log.d(TAG, "Loading rewarded ad with ID: $adUnitId")

        RewardedAd.load(
            context,
            adUnitId,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e(TAG, "Rewarded ad failed to load: ${adError.message}")
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded ad loaded successfully")
                    rewardedAd = ad
                    setupRewardedCallbacks(ad)
                }
            }
        )
    }

    private fun setupRewardedCallbacks(ad: RewardedAd) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "Rewarded ad clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                Log.d(TAG, "Rewarded ad dismissed")
                rewardedAd = null
                loadRewardedAd() // Precargar el siguiente
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                Log.e(TAG, "Rewarded ad failed to show: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                Log.d(TAG, "Rewarded ad showed full screen content")
            }
        }
    }

    fun showRewardedAd(
        activity: Activity,
        onUserEarnedReward: ((rewardType: String, rewardAmount: Int) -> Unit)? = null,
        onAdClosed: (() -> Unit)? = null
    ) {
        rewardedAd?.let { ad ->
            Log.d(TAG, "Showing rewarded ad")
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    rewardedAd = null
                    loadRewardedAd()
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.e(TAG, "Failed to show rewarded ad: ${adError.message}")
                    rewardedAd = null
                    onAdClosed?.invoke()
                }
            }

            ad.show(activity) { rewardItem ->
                Log.d(TAG, "User earned reward: ${rewardItem.type} x ${rewardItem.amount}")
                onUserEarnedReward?.invoke(rewardItem.type, rewardItem.amount)
            }
        } ?: run {
            Log.w(TAG, "Rewarded ad not ready")
            onAdClosed?.invoke()
            loadRewardedAd() // Intentar cargar uno nuevo
        }
    }

    // Verificar disponibilidad de anuncios
    fun isInterstitialAdReady(): Boolean = interstitialAd != null
    fun isRewardedAdReady(): Boolean = rewardedAd != null

    // Forzar recarga de anuncios
    fun reloadAds() {
        Log.d(TAG, "Reloading all ads")
        loadInterstitialAd()
        loadRewardedAd()
    }

    // Cambiar entre anuncios de prueba y reales (para testing)
    fun enableTestAds(enable: Boolean) {
        // Esta función permite cambiar fácilmente durante el desarrollo
        // No implementada en runtime para mantener la constante, pero la estructura está lista
        Log.d(TAG, "Note: To change ad mode, modify USE_TEST_ADS constant and rebuild")
    }
}