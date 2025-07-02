package com.upao.nutrialarm.presentation.meal

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
fun MealRecipeScreen(
    mealId: String,
    onNavigateBack: () -> Unit = {},
    viewModel: MealRecipeViewModel = hiltViewModel()
) {
    val meal by viewModel.meal.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(mealId) {
        viewModel.loadMeal(mealId)
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
            // Header de la receta
            AnimatedVisibility(
                visible = headerVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { -it / 2 }
                )
            ) {
                MealRecipeHeader(
                    meal = meal,
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
                            onRetry = { viewModel.loadMeal(mealId) }
                        )
                    }
                    meal != null -> {
                        MealRecipeContent(meal = meal!!)
                    }
                }
            }
        }
    }
}

@Composable
private fun MealRecipeHeader(
    meal: Meal?,
    onNavigateBack: () -> Unit
) {
    val mealTypeColor = when (meal?.mealType) {
        MealType.BREAKFAST -> NutriOrange
        MealType.SCHOOL_SNACK -> NutriGreen
        MealType.LUNCH -> NutriBlue
        MealType.AFTERNOON_SNACK -> NutriOrange.copy(alpha = 0.8f)
        MealType.DINNER -> Color(0xFF6366F1) // Púrpura
        MealType.OPTIONAL_SNACK -> NutriGray
        else -> NutriBlue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = mealTypeColor
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
                            mealTypeColor,
                            mealTypeColor.copy(alpha = 0.8f)
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
                            text = meal?.name ?: "Cargando...",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = meal?.mealType?.let { getMealDisplayName(it) } ?: "",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = meal?.mealType?.let { getMealTypeIcon(it) } ?: "🍽️",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = meal?.description ?: "",
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
                text = "Cargando receta...",
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
private fun MealRecipeContent(meal: Meal) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NutritionalInfoCard(meal = meal)
        }

        item {
            IngredientsCard(ingredients = meal.ingredients)
        }

        item {
            PreparationCard(meal = meal)
        }

        item {
            NutritionalBenefitsCard(meal = meal)
        }

        item {
            MealTipsCard()
        }
    }
}

@Composable
private fun NutritionalInfoCard(meal: Meal) {
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
                    text = "Información Nutricional",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NutritionalMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Hierro",
                    value = "${meal.ironContent}",
                    unit = "mg",
                    color = IronRed,
                    icon = "🩸"
                )

                NutritionalMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Calorías",
                    value = "${meal.calories.toInt()}",
                    unit = "cal",
                    color = NutriOrange,
                    icon = "🔥"
                )

                NutritionalMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Tiempo",
                    value = "${meal.preparationTime}",
                    unit = "min",
                    color = NutriGreen,
                    icon = "⏱️"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NutritionalMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Vitamina C",
                    value = "${meal.vitaminC}",
                    unit = "mg",
                    color = VitaminOrange,
                    icon = "🍊"
                )

                NutritionalMetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Folato",
                    value = "${meal.folate}",
                    unit = "mcg",
                    color = ProteinPurple,
                    icon = "🥬"
                )

                // Espacio vacío para mantener el layout
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun NutritionalMetricCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    unit: String,
    color: Color,
    icon: String
) {
    Card(
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
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
                text = icon,
                fontSize = 16.sp
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = "$label ${if (unit.isNotEmpty()) "($unit)" else ""}",
                fontSize = 10.sp,
                color = color.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun IngredientsCard(ingredients: List<String>) {
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
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = NutriGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ingredientes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (ingredients.isEmpty()) {
                Text(
                    text = "No hay ingredientes listados",
                    fontSize = 14.sp,
                    color = NutriGray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                ingredients.forEachIndexed { index, ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            color = NutriGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = ingredient,
                            fontSize = 14.sp,
                            color = NutriGrayDark,
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreparationCard(meal: Meal) {
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
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = NutriBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Preparación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información básica de preparación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = NutriOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tiempo: ${meal.preparationTime} minutos",
                        fontSize = 14.sp,
                        color = NutriGrayDark,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = NutriBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1 porción",
                        fontSize = 14.sp,
                        color = NutriGrayDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pasos básicos de preparación (genéricos)
            val basicSteps = getBasicPreparationSteps(meal)

            basicSteps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                NutriBlue.copy(alpha = 0.2f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutriBlue
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = step,
                        fontSize = 14.sp,
                        color = NutriGrayDark,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NutritionalBenefitsCard(meal: Meal) {
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
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = IronRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Beneficios Nutricionales",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val benefits = getNutritionalBenefits(meal)

            benefits.forEach { benefit ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "✓",
                        color = NutriGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        fontSize = 14.sp,
                        color = NutriGrayDark,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MealTipsCard() {
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
                    text = "Consejos de Preparación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            listOf(
                "Lava bien todos los ingredientes antes de usar",
                "Acompaña con frutas ricas en vitamina C para mejor absorción de hierro",
                "Evita combinar con té o café durante la comida",
                "Almacena las sobras en refrigeración por máximo 2 días"
            ).forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "💡",
                        fontSize = 14.sp
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

private fun getBasicPreparationSteps(meal: Meal): List<String> {
    return when (meal.mealType) {
        MealType.BREAKFAST -> listOf(
            "Prepara todos los ingredientes y ten los utensilios listos",
            "Cocina los ingredientes base según las indicaciones",
            "Combina los ingredientes manteniendo las proporciones",
            "Sirve caliente y acompaña con frutas frescas"
        )
        MealType.SCHOOL_SNACK -> listOf(
            "Lava y prepara frutas y vegetales",
            "Mezcla ingredientes en un recipiente limpio",
            "Empaca en contenedor hermético para llevar",
            "Mantén refrigerado hasta el consumo"
        )
        MealType.LUNCH -> listOf(
            "Organiza ingredientes por orden de cocción",
            "Cocina proteínas y cereales según tiempos necesarios",
            "Prepara vegetales al vapor o salteados",
            "Combina todos los elementos en el plato",
            "Sirve acompañado de ensalada fresca"
        )
        MealType.AFTERNOON_SNACK -> listOf(
            "Prepara ingredientes frescos",
            "Combina elementos secos y húmedos",
            "Sirve en porciones adecuadas",
            "Acompaña con líquidos saludables"
        )
        MealType.DINNER -> listOf(
            "Prepara una comida ligera pero nutritiva",
            "Cocina a fuego medio para conservar nutrientes",
            "Combina proteínas con vegetales",
            "Sirve 2-3 horas antes de dormir"
        )
        MealType.OPTIONAL_SNACK -> listOf(
            "Evalúa si realmente necesitas este snack",
            "Prepara una porción pequeña",
            "Elige opciones saludables y ligeras",
            "Consume con moderación"
        )
    }
}

private fun getNutritionalBenefits(meal: Meal): List<String> {
    val benefits = mutableListOf<String>()

    if (meal.ironContent > 3.0) {
        benefits.add("Rico en hierro - ayuda a prevenir la anemia")
    }
    if (meal.vitaminC > 30.0) {
        benefits.add("Alto contenido de vitamina C - mejora la absorción de hierro")
    }
    if (meal.folate > 50.0) {
        benefits.add("Fuente de folato - importante para la formación de glóbulos rojos")
    }
    if (meal.calories > 300.0) {
        benefits.add("Aporta energía suficiente para las actividades diarias")
    }

    // Beneficios adicionales genéricos
    benefits.add("Contribuye a una alimentación balanceada")
    benefits.add("Forma parte de una dieta antianoemia")

    return benefits
}

@Preview(showBackground = true)
@Composable
fun MealRecipeScreenPreview() {
    NutriAlarmTheme {
        MealRecipeScreen(mealId = "test")
    }
}