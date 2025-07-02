package com.upao.nutrialarm.presentation.diet

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.*
import com.upao.nutrialarm.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietDetailScreen(
    dietId: String,
    onNavigateBack: () -> Unit = {},
    onMealClick: (String) -> Unit = {},
    viewModel: DietDetailViewModel = hiltViewModel()
) {
    val diet by viewModel.diet.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(dietId) {
        viewModel.loadDiet(dietId)
        headerVisible = true
        delay(200)
        contentVisible = true
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
            // Header dinámico basado en la dieta
            AnimatedVisibility(
                visible = headerVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { -it / 2 }
                )
            ) {
                DietDetailHeader(
                    diet = diet,
                    onNavigateBack = onNavigateBack
                )
            }

            // Contenido principal
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                when {
                    isLoading -> {
                        LoadingContent()
                    }
                    error != null -> {
                        ErrorContent(
                            error = error!!,
                            onRetry = { viewModel.loadDiet(dietId) }
                        )
                    }
                    diet != null -> {
                        DietDetailContent(
                            diet = diet!!,
                            onMealClick = onMealClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DietDetailHeader(
    diet: Diet?,
    onNavigateBack: () -> Unit
) {
    val riskColors = when (diet?.anemiaRiskLevel) {
        AnemiaRisk.LOW -> Triple(NutriGreen, NutriGreen.copy(alpha = 0.1f), "Riesgo Bajo")
        AnemiaRisk.MEDIUM -> Triple(NutriOrange, NutriOrange.copy(alpha = 0.1f), "Riesgo Medio")
        AnemiaRisk.HIGH -> Triple(NutriRed, NutriRed.copy(alpha = 0.1f), "Riesgo Alto")
        else -> Triple(NutriBlue, NutriBlue.copy(alpha = 0.1f), "Cargando...")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = riskColors.first
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
                            riskColors.first,
                            riskColors.first.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
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
                            text = diet?.name ?: "Cargando...",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = riskColors.third,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = "🍽️",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = diet?.description ?: "",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = NutriBlue,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Cargando detalles de la dieta...",
                color = NutriGray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = NutriRed,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = error,
                    color = NutriGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
private fun DietDetailContent(
    diet: Diet,
    onMealClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NutritionalSummaryCard(diet = diet)
        }

        item {
            Text(
                text = "Comidas de la Dieta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
            )
        }

        if (diet.meals.isEmpty()) {
            item {
                EmptyMealsCard()
            }
        } else {
            // Agrupar comidas por tipo
            val mealsByType = diet.meals.groupBy { it.mealType }

            items(MealType.values().toList()) { mealType ->
                val mealsForType = mealsByType[mealType]
                if (!mealsForType.isNullOrEmpty()) {
                    MealTypeSection(
                        mealType = mealType,
                        meals = mealsForType,
                        onMealClick = onMealClick
                    )
                }
            }
        }

        item {
            DietTipsCard()
        }
    }
}

@Composable
private fun NutritionalSummaryCard(diet: Diet) {
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
                    Icons.Default.Analytics,
                    contentDescription = null,
                    tint = NutriBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Resumen Nutricional",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    listOf(
                        NutrientSummary("Hierro Total", "${diet.ironContent}", "mg", IronRed, "🩸"),
                        NutrientSummary("Calorías", "${diet.calories.toInt()}", "cal", NutriOrange, "🔥"),
                        NutrientSummary("Comidas", "${diet.meals.size}", "", NutriBlue, "🍽️"),
                        NutrientSummary("Tiempo Prep", "${diet.meals.sumOf { it.preparationTime }}", "min", NutriGreen, "⏱️")
                    )
                ) { nutrient ->
                    NutrientSummaryCard(nutrient)
                }
            }
        }
    }
}

@Composable
private fun NutrientSummaryCard(nutrient: NutrientSummary) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = nutrient.color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = nutrient.icon,
                fontSize = 20.sp
            )
            Text(
                text = nutrient.value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = nutrient.color
            )
            Text(
                text = "${nutrient.label} ${nutrient.unit}",
                fontSize = 10.sp,
                color = nutrient.color.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MealTypeSection(
    mealType: MealType,
    meals: List<Meal>,
    onMealClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = getMealTypeIcon(mealType),
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = getMealDisplayName(mealType),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = "${meals.size} opción${if (meals.size > 1) "es" else ""}",
                        fontSize = 12.sp,
                        color = NutriGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            meals.forEach { meal ->
                MealItemCard(
                    meal = meal,
                    onClick = { onMealClick(meal.id) }
                )
                if (meal != meals.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealItemCard(
    meal: Meal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = NutriBlue.copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutriGrayDark
                )
                Text(
                    text = meal.description,
                    fontSize = 12.sp,
                    color = NutriGray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NutrientBadge(
                        value = "${meal.ironContent}mg",
                        color = IronRed,
                        icon = "🩸"
                    )
                    NutrientBadge(
                        value = "${meal.calories.toInt()}cal",
                        color = NutriOrange,
                        icon = "🔥"
                    )
                    NutrientBadge(
                        value = "${meal.preparationTime}min",
                        color = NutriGreen,
                        icon = "⏱️"
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = NutriGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun NutrientBadge(
    value: String,
    color: Color,
    icon: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = icon,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = value,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@Composable
private fun EmptyMealsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🍽️",
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No hay comidas en esta dieta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark
            )

            Text(
                text = "Las comidas se cargarán pronto",
                fontSize = 14.sp,
                color = NutriGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DietTipsCard() {
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
                    text = "Consejos para esta Dieta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            listOf(
                "Consume las comidas en los horarios establecidos",
                "Acompaña alimentos ricos en hierro con vitamina C",
                "Evita té y café durante las comidas principales",
                "Mantente hidratado durante todo el día"
            ).forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "•",
                        color = NutriOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tip,
                        fontSize = 14.sp,
                        color = NutriGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

// Data classes auxiliares
private data class NutrientSummary(
    val label: String,
    val value: String,
    val unit: String,
    val color: Color,
    val icon: String
)

// Funciones auxiliares
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

@Preview(showBackground = true)
@Composable
fun DietDetailScreenPreview() {
    NutriAlarmTheme {
        DietDetailScreen(dietId = "test")
    }
}