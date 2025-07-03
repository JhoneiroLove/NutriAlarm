package com.upao.nutrialarm.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.upao.nutrialarm.ui.theme.NutriBlue
import com.upao.nutrialarm.ui.theme.NutriGray
import com.upao.nutrialarm.ui.theme.NutriGrayDark

@Composable
fun LoadingDialog(
    isVisible: Boolean,
    message: String = "Cargando...",
    onDismiss: (() -> Unit)? = null
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = { onDismiss?.invoke() },
            properties = DialogProperties(
                dismissOnBackPress = onDismiss != null,
                dismissOnClickOutside = false
            )
        ) {
            AnimatedLoadingContent(message = message)
        }
    }
}

@Composable
private fun AnimatedLoadingContent(message: String) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(
            animationSpec = tween(300),
            initialScale = 0.8f
        ),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(
            animationSpec = tween(300)
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Indicador de progreso animado
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = NutriBlue,
                    strokeWidth = 4.dp
                )

                // Mensaje
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = NutriGrayDark
                )

                // Texto secundario animado
                AnimatedLoadingText()
            }
        }
    }
}

@Composable
private fun AnimatedLoadingText() {
    val loadingTexts = listOf("Procesando", "Procesando.", "Procesando..", "Procesando...")
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(500)
            currentIndex = (currentIndex + 1) % loadingTexts.size
        }
    }

    Text(
        text = loadingTexts[currentIndex],
        fontSize = 14.sp,
        color = NutriGray
    )
}
