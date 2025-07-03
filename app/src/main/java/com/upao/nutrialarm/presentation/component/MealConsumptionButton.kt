package com.upao.nutrialarm.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.ui.theme.NutriGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MealConsumptionButton(
    mealType: MealType,
    isConsumed: Boolean,
    onMarkAsConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var isAnimating by remember { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if (isConsumed) NutriGreen else Color.White.copy(alpha = 0.9f),
        animationSpec = tween(300)
    )

    val contentColor by animateColorAsState(
        targetValue = if (isConsumed) Color.White else NutriGreen,
        animationSpec = tween(300)
    )

    Button(
        onClick = {
            if (!isConsumed && enabled) {
                isAnimating = true
                onMarkAsConsumed()
            }
        },
        enabled = enabled && !isConsumed,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = contentColor,
            disabledContainerColor = NutriGreen.copy(alpha = 0.6f),
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = isConsumed,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
            }
        ) { consumed ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (consumed) Icons.Default.CheckCircle else Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (consumed) "¡Consumida!" else "Marcar como consumida",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            delay(100)
            isAnimating = false
        }
    }
}