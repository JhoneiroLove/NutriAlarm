
package com.upao.nutrialarm.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.usecase.meal.NextMealInfo
import com.upao.nutrialarm.domain.usecase.meal.DailyProgress
import com.upao.nutrialarm.domain.usecase.meal.ProgressItem
import com.upao.nutrialarm.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicHomeScreen(
    onNavigateToDiets: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val currentUser by homeViewModel.currentUser.collectAsState()
    val nextMealInfo by homeViewModel.nextMealInfo.collectAsState()
    val dailyProgress by homeViewModel.dailyProgress.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val message by homeViewModel.message.collectAsState()

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var nextMealVisible by remember { mutableStateOf(false) }
    var statsVisible by remember { mutableStateOf(false) }
    var menuVisible by remember { mutableStateOf(false) }

    // Auto-clear messages después de 3 segundos
    LaunchedEffect(message) {
        if (message != null) {
            delay(3000)
            homeViewModel.clearMessage()
        }
    }

    LaunchedEffect(Unit) {
        headerVisible = true
        delay(200)
        nextMealVisible = true
        delay(200)
        statsVisible = true
        delay(200)
        menuVisible = true

        // Actualizar cada minuto
        while (true) {
            delay(60_000L)
            homeViewModel.refreshData()
        }
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = headerVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { -it / 2 }
                    )
                ) {
                    DynamicHeaderSection(
                        currentUser = currentUser,
                        onRefresh = { homeViewModel.refreshData() },
                        isRefreshing = isLoading
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = nextMealVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInHorizontally(
                        animationSpec = tween(600),
                        initialOffsetX = { -it / 2 }
                    )
                ) {
                    DynamicNextMealCard(
                        nextMealInfo = nextMealInfo,
                        onMealConsumed = { homeViewModel.markMealAsConsumed() },
                        isLoading = isLoading
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = statsVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { it / 2 }
                    )
                ) {
                    DynamicProgressSection(
                        dailyProgress = dailyProgress,
                        isLoading = isLoading
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = menuVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { it / 2 }
                    )
                ) {
                    QuickActionsSection(
                        onNavigateToDiets = onNavigateToDiets,
                        onNavigateToProfile = onNavigateToProfile
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = menuVisible,
                    enter = fadeIn(animationSpec = tween(800))
                ) {
                    DynamicTipsSection(currentUser = currentUser)
                }
            }

            // Mostrar mensaje si existe
            message?.let { msg ->
                item {
                    MessageCard(
                        message = msg,
                        onDismiss = { homeViewModel.clearMessage() }
                    )
                }
            }
        }

        // Loading overlay mejorado
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = NutriGreen,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Actualizando...",
                            fontSize = 16.sp,
                            color = NutriGrayDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicHeaderSection(
    currentUser: User?,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    val currentTime = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
    val greeting = getGreeting()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NutriBlue
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NutriBlue,
                            Color(0xFF60A5FA)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = greeting,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = currentUser?.name ?: "Usuario",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Hora actual: $currentTime",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "🩸",
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón de refresh mejorado
                    IconButton(
                        onClick = onRefresh,
                        enabled = !isRefreshing,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Actualizar datos",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DynamicNextMealCard(
    nextMealInfo: NextMealInfo?,
    onMealConsumed: () -> Unit,
    isLoading: Boolean
) {
    val cardColor = when (nextMealInfo?.mealType) {
        MealType.BREAKFAST -> NutriOrange
        MealType.SCHOOL_SNACK -> NutriGreen
        MealType.LUNCH -> NutriBlue
        MealType.AFTERNOON_SNACK -> Color(0xFFFF7043)
        MealType.DINNER -> Color(0xFF6366F1)
        MealType.OPTIONAL_SNACK -> NutriGray
        else -> NutriGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            cardColor,
                            cardColor.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            if (nextMealInfo != null) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = if (nextMealInfo.hasBeenConsumed) "¡Comida completada!" else "Próxima comida",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = getMealDisplayName(nextMealInfo.mealType),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${nextMealInfo.timeSlot} ${if (nextMealInfo.isToday) "- Hoy" else "- Mañana"}",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Text(
                            text = getMealTypeIcon(nextMealInfo.mealType),
                            fontSize = 32.sp
                        )
                    }

                    if (!nextMealInfo.hasBeenConsumed) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onMealConsumed,
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.9f),
                                contentColor = cardColor,
                                disabledContainerColor = Color.White.copy(alpha = 0.5f),
                                disabledContentColor = cardColor.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = cardColor,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Procesando...")
                            } else {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Marcar como consumida",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Mostrar mensaje de completado
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "¡Ya consumiste esta comida!",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            } else {
                // Estado de carga
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cargando próxima comida...",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DynamicProgressSection(
    dailyProgress: DailyProgress?,
    isLoading: Boolean
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
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Progreso de Hoy",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )

                Text(
                    text = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date()),
                    fontSize = 12.sp,
                    color = NutriGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (dailyProgress != null && !isLoading) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        listOf(
                            dailyProgress.iron,
                            dailyProgress.calories,
                            dailyProgress.meals,
                            dailyProgress.water
                        )
                    ) { progressItem ->
                        DynamicProgressCard(progressItem)
                    }
                }
            } else {
                // Estado de carga
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(100.dp)
                                .background(
                                    NutriGray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = NutriGray,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicProgressCard(progressItem: ProgressItem) {
    val color = when (progressItem.label) {
        "Hierro" -> IronRed
        "Calorías" -> NutriOrange
        "Comidas" -> NutriBlue
        "Agua" -> Color(0xFF06B6D4)
        else -> NutriGray
    }

    val icon = when (progressItem.label) {
        "Hierro" -> "🩸"
        "Calorías" -> "🔥"
        "Comidas" -> "🍽️"
        "Agua" -> "💧"
        else -> "📊"
    }

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = progressItem.label,
                fontSize = 12.sp,
                color = color,
                fontWeight = FontWeight.Medium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = progressItem.formattedCurrent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = "/${progressItem.formattedTarget}",
                    fontSize = 10.sp,
                    color = color.copy(alpha = 0.7f)
                )
            }

            if (progressItem.unit.isNotEmpty()) {
                Text(
                    text = progressItem.unit,
                    fontSize = 10.sp,
                    color = color.copy(alpha = 0.6f)
                )
            }

            // Barra de progreso
            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { (progressItem.current / progressItem.target).toFloat().coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun DynamicTipsSection(currentUser: User?) {
    val tips = getDynamicTips(currentUser)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
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
                    text = "Consejo Personalizado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = tips,
                fontSize = 14.sp,
                color = NutriGray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun QuickActionsSection(
    onNavigateToDiets: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column {
        Text(
            text = "Acciones Rápidas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NutriGrayDark,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                modifier = Modifier.weight(1f),
                title = "Ver Dietas",
                subtitle = "Dietas personalizadas",
                icon = Icons.Default.Restaurant,
                color = NutriBlue,
                onClick = onNavigateToDiets
            )

            ActionCard(
                modifier = Modifier.weight(1f),
                title = "Mi Perfil",
                subtitle = "Configurar alarmas",
                icon = Icons.Default.Person,
                color = NutriGreen,
                onClick = onNavigateToProfile
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(120.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color.copy(alpha = 0.1f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = NutriGray
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageCard(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NutriGreen.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = NutriGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = NutriGreen,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = NutriGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Funciones auxiliares
private fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "¡Buenos días!"
        in 12..17 -> "¡Buenas tardes!"
        in 18..23 -> "¡Buenas noches!"
        else -> "¡Hola!"
    }
}

private fun getMealDisplayName(mealType: MealType): String {
    return when (mealType) {
        MealType.BREAKFAST -> "Desayuno"
        MealType.SCHOOL_SNACK -> "Refrigerio Escolar"
        MealType.LUNCH -> "Almuerzo"
        MealType.AFTERNOON_SNACK -> "Merienda de Tarde"
        MealType.DINNER -> "Cena"
        MealType.OPTIONAL_SNACK -> "Snack Opcional"
    }
}

private fun getMealTypeIcon(mealType: MealType): String {
    return when (mealType) {
        MealType.BREAKFAST -> "🌅"
        MealType.SCHOOL_SNACK -> "🍎"
        MealType.LUNCH -> "🍽️"
        MealType.AFTERNOON_SNACK -> "🥜"
        MealType.DINNER -> "🌙"
        MealType.OPTIONAL_SNACK -> "☕"
    }
}

private fun getDynamicTips(user: User?): String {
    val tips = listOf(
        "Combina alimentos ricos en hierro con frutas cítricas para mejor absorción.",
        "Evita combinar té o café con comidas ricas en hierro.",
        "Las lentejas son una excelente fuente de hierro vegetal.",
        "Incluye vegetales verdes oscuros en tu dieta diaria.",
        "La vitamina C mejora significativamente la absorción de hierro.",
        "Mantén horarios regulares de comida para mejor nutrición.",
        "Hidrátate bien entre comidas, no durante las comidas.",
        "Los frutos secos son un excelente snack rico en hierro."
    )

    val personalizedTips = if (user != null) {
        when (user.anemiaRisk) {
            com.upao.nutrialarm.domain.model.AnemiaRisk.HIGH ->
                "Dado tu alto riesgo de anemia, asegúrate de consumir al menos 18mg de hierro diarios. ${tips.random()}"
            com.upao.nutrialarm.domain.model.AnemiaRisk.MEDIUM ->
                "Con tu riesgo medio de anemia, consume al menos 12mg de hierro al día. ${tips.random()}"
            else -> tips.random()
        }
    } else {
        tips.random()
    }

    return personalizedTips
}

@Preview(showBackground = true)
@Composable
fun DynamicHomeScreenPreview() {
    NutriAlarmTheme {
        DynamicHomeScreen()
    }
}