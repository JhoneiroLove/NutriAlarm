package com.upao.nutrialarm.presentation.component

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdView
import com.upao.nutrialarm.data.remote.admob.AdMobService
import com.upao.nutrialarm.ui.theme.NutriGray

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adMobService: AdMobService? = null
) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<AdView?>(null) }
    var isAdLoaded by remember { mutableStateOf(false) }
    var isAdError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (adMobService != null) {
            try {
                val newAdView = adMobService.createBannerAd()

                // Configurar listener personalizado para este banner
                newAdView.adListener = object : com.google.android.gms.ads.AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        isAdLoaded = true
                        isAdError = false
                    }

                    override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        isAdLoaded = false
                        isAdError = true
                    }
                }

                adView = newAdView
                adMobService.loadBannerAd(newAdView)
            } catch (e: Exception) {
                isAdError = true
            }
        } else {
            isAdError = true
        }
    }

    if (isAdError) {
        // Mostrar placeholder cuando hay error
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = CardDefaults.cardColors(
                containerColor = NutriGray.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Espacio publicitario",
                    color = NutriGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    } else if (isAdLoaded && adView != null) {
        // Mostrar el anuncio cuando está cargado
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            AndroidView(
                factory = { adView!! },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(4.dp)
            )
        }
    } else {
        // Mostrar loading mientras carga
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = CardDefaults.cardColors(
                containerColor = NutriGray.copy(alpha = 0.05f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = NutriGray
                )
            }
        }
    }

    DisposableEffect(adView) {
        onDispose {
            adView?.destroy()
        }
    }
}