package com.upao.nutrialarm.presentation.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.usecase.user.HealthStatus
import com.upao.nutrialarm.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthStatusScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: HealthStatusViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
        delay(200)
        contentVisible = true
        viewModel.loadHealthStatus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFC),
                        Color(0xFFE2E8F0)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            AnimatedVisibility(
                visible = headerVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { -it / 2 }
                )
            ) {
                HealthStatusHeader(onNavigateBack = onNavigateBack)
            }

            // Contenido
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                if (uiState.isLoading) {
                    LoadingContent()
                } else if (uiState.user != null && uiState.healthStatus != null) {
                    HealthStatusContent(
                        user = uiState.user!!,
                        healthStatus = uiState.healthStatus!!
                    )
                } else {
                    ErrorContent(
                        message = uiState.errorMessage ?: "No se pudo cargar la información",
                        onRetry = { viewModel.loadHealthStatus() }
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthStatusHeader(onNavigateBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = NutriRed
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NutriRed,
                            Color(0xFFEF4444)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estado de Salud",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Evaluación nutricional y riesgo",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "🩺",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
private fun HealthStatusContent(
    user: User,
    healthStatus: HealthStatus
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            UserHealthSummaryCard(user = user, healthStatus = healthStatus)
        }

        item {
            BMIStatusCard(healthStatus = healthStatus)
        }

        item {
            AnemiaRiskCard(anemiaRisk = user.anemiaRisk)
        }

        item {
            NutritionalNeedsCard(healthStatus = healthStatus)
        }

        item {
            HealthRecommendationsCard(user = user, healthStatus = healthStatus)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun UserHealthSummaryCard(
    user: User,
    healthStatus: HealthStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NutriBlue.copy(alpha = 0.1f),
                            NutriBlue.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Resumen General",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutriGrayDark
                        )
                        Text(
                            text = user.name,
                            fontSize = 16.sp,
                            color = NutriGray
                        )
                    }

                    Text(
                        text = "📋",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HealthMetric(
                        label = "Edad",
                        value = "${user.age} años",
                        icon = "🎂"
                    )
                    HealthMetric(
                        label = "Peso",
                        value = "${user.weight} kg",
                        icon = "⚖️"
                    )
                    HealthMetric(
                        label = "Altura",
                        value = "${user.height} cm",
                        icon = "📏"
                    )
                }
            }
        }
    }
}

@Composable
private fun BMIStatusCard(healthStatus: HealthStatus) {
    val bmiColor = when (healthStatus.bmiCategory) {
        "Bajo peso" -> Color(0xFF3B82F6)
        "Peso normal" -> NutriGreen
        "Sobrepeso" -> NutriOrange
        "Obesidad" -> NutriRed
        else -> NutriGray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.MonitorWeight,
                    contentDescription = null,
                    tint = bmiColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Índice de Masa Corporal (IMC)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = String.format("%.1f", healthStatus.bmi),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = bmiColor
                    )
                    Text(
                        text = "kg/m²",
                        fontSize = 14.sp,
                        color = NutriGray
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = bmiColor.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = healthStatus.bmiCategory,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = bmiColor,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = getBMIDescription(healthStatus.bmiCategory),
                fontSize = 14.sp,
                color = NutriGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AnemiaRiskCard(anemiaRisk: AnemiaRisk) {
    val riskColor = when (anemiaRisk) {
        AnemiaRisk.LOW -> NutriGreen
        AnemiaRisk.MEDIUM -> NutriOrange
        AnemiaRisk.HIGH -> NutriRed
    }

    val riskText = when (anemiaRisk) {
        AnemiaRisk.LOW -> "Riesgo Bajo"
        AnemiaRisk.MEDIUM -> "Riesgo Medio"
        AnemiaRisk.HIGH -> "Riesgo Alto"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = riskColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Riesgo de Anemia",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🩸",
                    fontSize = 40.sp
                )

                Column(horizontalAlignment = Alignment.End) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = riskColor.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = riskText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = riskColor,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = getAnemiaRiskDescription(anemiaRisk),
                fontSize = 14.sp,
                color = NutriGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun NutritionalNeedsCard(healthStatus: HealthStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocalDining,
                    contentDescription = null,
                    tint = NutriBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Necesidades Nutricionales",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutritionalNeed(
                    label = "Hierro Diario",
                    value = "${healthStatus.recommendedIronIntake.toInt()} mg",
                    color = IronRed,
                    icon = "🩸"
                )

                NutritionalNeed(
                    label = "Calorías Diarias",
                    value = "${healthStatus.recommendedCalories.toInt()} cal",
                    color = NutriOrange,
                    icon = "🔥"
                )
            }
        }
    }
}

@Composable
private fun HealthRecommendationsCard(
    user: User,
    healthStatus: HealthStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = NutriOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recomendaciones Personalizadas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val recommendations = getPersonalizedRecommendations(user, healthStatus)
            recommendations.forEachIndexed { index, recommendation ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        color = NutriBlue,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = recommendation,
                        fontSize = 14.sp,
                        color = NutriGray,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthMetric(
    label: String,
    value: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NutriGrayDark
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = NutriGray
        )
    }
}

@Composable
private fun NutritionalNeed(
    label: String,
    value: String,
    color: Color,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = NutriGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = NutriBlue,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Evaluando tu estado de salud...",
                fontSize = 16.sp,
                color = NutriGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Error,
                    contentDescription = null,
                    tint = NutriRed,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = NutriGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriBlue
                    )
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}

private fun getBMIDescription(category: String): String {
    return when (category) {
        "Bajo peso" -> "Tu IMC indica que podrías beneficiarte de ganar peso de manera saludable. Consulta con un profesional de la salud."
        "Peso normal" -> "¡Excelente! Tu IMC está en el rango saludable. Mantén tus hábitos alimentarios y de ejercicio."
        "Sobrepeso" -> "Tu IMC indica sobrepeso. Considera adoptar hábitos más saludables y consulta con un profesional."
        "Obesidad" -> "Tu IMC indica obesidad. Es importante que consultes con un profesional de la salud para un plan personalizado."
        else -> "Evaluación de IMC no disponible."
    }
}

private fun getAnemiaRiskDescription(risk: AnemiaRisk): String {
    return when (risk) {
        AnemiaRisk.LOW -> "Tu riesgo de anemia es bajo. Mantén una dieta balanceada rica en hierro como medida preventiva."
        AnemiaRisk.MEDIUM -> "Tienes un riesgo medio de anemia. Es importante que sigas las recomendaciones nutricionales y monitorees tu salud."
        AnemiaRisk.HIGH -> "Tu riesgo de anemia es alto. Sigue estrictamente las recomendaciones y considera consultar con un profesional de la salud."
    }
}

private fun getPersonalizedRecommendations(user: User, healthStatus: HealthStatus): List<String> {
    val recommendations = mutableListOf<String>()

    // Recomendaciones basadas en IMC
    when (healthStatus.bmiCategory) {
        "Bajo peso" -> {
            recommendations.add("Aumenta tu ingesta calórica con alimentos nutritivos como frutos secos, aguacates y proteínas magras")
            recommendations.add("Considera hacer ejercicio de fuerza para ganar masa muscular saludable")
        }
        "Sobrepeso", "Obesidad" -> {
            recommendations.add("Enfócate en una alimentación balanceada con porciones controladas")
            recommendations.add("Incrementa tu actividad física gradualmente. Comienza con 30 minutos de caminata diaria")
        }
        "Peso normal" -> {
            recommendations.add("Mantén tu peso actual con una dieta equilibrada y ejercicio regular")
        }
    }

    // Recomendaciones basadas en riesgo de anemia
    when (user.anemiaRisk) {
        AnemiaRisk.HIGH -> {
            recommendations.add("Consume diariamente alimentos ricos en hierro como lentejas, espinacas y carne magra")
            recommendations.add("Combina alimentos con hierro con vitamina C (cítricos, tomates) para mejorar la absorción")
            recommendations.add("Evita el té y café durante las comidas principales")
        }
        AnemiaRisk.MEDIUM -> {
            recommendations.add("Incluye al menos una fuente de hierro en cada comida principal")
            recommendations.add("Consume frutas cítricas como postre para mejorar la absorción del hierro")
        }
        AnemiaRisk.LOW -> {
            recommendations.add("Mantén una dieta variada que incluya fuentes naturales de hierro")
        }
    }

    // Recomendaciones basadas en nivel de actividad
    recommendations.add("Mantén tu nivel de actividad física actual: ${getActivityRecommendation(user.activityLevel)}")

    return recommendations
}

private fun getActivityRecommendation(activityLevel: com.upao.nutrialarm.domain.model.ActivityLevel): String {
    return when (activityLevel) {
        com.upao.nutrialarm.domain.model.ActivityLevel.SEDENTARY -> "Intenta incorporar al menos 30 minutos de actividad ligera diaria"
        com.upao.nutrialarm.domain.model.ActivityLevel.LIGHT -> "Aumenta gradualmente tu actividad física a 45 minutos diarios"
        com.upao.nutrialarm.domain.model.ActivityLevel.MODERATE -> "Excelente nivel de actividad, mantén la consistencia"
        com.upao.nutrialarm.domain.model.ActivityLevel.ACTIVE -> "Muy buen nivel de actividad, considera añadir ejercicios de fuerza"
        com.upao.nutrialarm.domain.model.ActivityLevel.VERY_ACTIVE -> "Nivel de actividad excelente, asegúrate de descansar adecuadamente"
    }
}

@Preview(showBackground = true)
@Composable
fun HealthStatusScreenPreview() {
    NutriAlarmTheme {
        HealthStatusScreen()
    }
}