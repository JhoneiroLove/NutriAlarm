package com.upao.nutrialarm.data.remote.admob

import android.app.Activity
import android.content.Context
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
        // IDs de prueba - Reemplaza con tus IDs reales de AdMob
        private const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111" // Test Banner
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712" // Test Interstitial
        private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917" // Test Rewarded
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
            adUnitId = BANNER_AD_UNIT_ID
            setAdSize(AdSize.BANNER)

            // Configurar listener para eventos del banner
            adListener = object : AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    // Analytics: track banner click
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    // Analytics: track banner loaded
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    // Log error
                }
            }
        }
    }

    fun loadBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    // Interstitial Ads
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    // Log error
                }

                override fun onAdLoaded(ad: InterstitialAd) {
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
                // Analytics: track interstitial click
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                interstitialAd = null
                loadInterstitialAd() // Precargar el siguiente
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                interstitialAd = null
            }

            override fun onAdImpression() {
                super.onAdImpression()
                // Analytics: track impression
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                // Analytics: track show
            }
        }
    }

    fun showInterstitialAd(activity: Activity, onAdClosed: (() -> Unit)? = null) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    interstitialAd = null
                    loadInterstitialAd()
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    interstitialAd = null
                    onAdClosed?.invoke()
                }
            }
            ad.show(activity)
        } ?: run {
            // No hay anuncio disponible
            onAdClosed?.invoke()
            loadInterstitialAd() // Intentar cargar uno nuevo
        }
    }

    // Rewarded Ads
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    // Log error
                }

                override fun onAdLoaded(ad: RewardedAd) {
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
                // Analytics: track rewarded click
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                rewardedAd = null
                loadRewardedAd() // Precargar el siguiente
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                rewardedAd = null
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                // Analytics: track show
            }
        }
    }

    fun showRewardedAd(
        activity: Activity,
        onUserEarnedReward: ((rewardType: String, rewardAmount: Int) -> Unit)? = null,
        onAdClosed: (() -> Unit)? = null
    ) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    rewardedAd = null
                    loadRewardedAd()
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    rewardedAd = null
                    onAdClosed?.invoke()
                }
            }

            ad.show(activity) { rewardItem ->
                // Usuario ganó la recompensa
                onUserEarnedReward?.invoke(rewardItem.type, rewardItem.amount)
            }
        } ?: run {
            // No hay anuncio disponible
            onAdClosed?.invoke()
            loadRewardedAd() // Intentar cargar uno nuevo
        }
    }

    // Verificar disponibilidad de anuncios
    fun isInterstitialAdReady(): Boolean = interstitialAd != null
    fun isRewardedAdReady(): Boolean = rewardedAd != null

    // Forzar recarga de anuncios
    fun reloadAds() {
        loadInterstitialAd()
        loadRewardedAd()
    }
}