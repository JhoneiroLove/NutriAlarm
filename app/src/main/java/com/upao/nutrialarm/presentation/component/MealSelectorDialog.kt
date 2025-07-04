package com.upao.nutrialarm.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.upao.nutrialarm.domain.model.Meal
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.ui.theme.*

@Composable
fun MealSelectorDialog(
    mealType: MealType,
    availableMeals: List<Meal>,
    onMealSelected: (Meal) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                MealSelectorHeader(
                    mealType = mealType,
                    onDismiss = onDismiss
                )

                // Lista de comidas
                if (availableMeals.isEmpty()) {
                    EmptyMealsState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(availableMeals) { meal ->
                            MealSelectionCard(
                                meal = meal,
                                onSelect = { onMealSelected(meal) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MealSelectorHeader(
    mealType: MealType,
    onDismiss: () -> Unit
) {
    val mealTypeColor = getMealTypeColor(mealType)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = mealTypeColor,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Seleccionar ${getMealDisplayName(mealType)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Elige qué comida específica consumiste",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White.copy(alpha = 0.2f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealSelectionCard(
    meal: Meal,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, NutriGray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meal.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutriGrayDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = meal.description,
                        fontSize = 14.sp,
                        color = NutriGray,
                        lineHeight = 18.sp
                    )
                }

                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = getMealTypeColor(meal.mealType),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información nutricional compacta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactNutrientBadge(
                    value = "${meal.ironContent}mg",
                    label = "Hierro",
                    color = IronRed,
                    icon = "🩸"
                )
                CompactNutrientBadge(
                    value = "${meal.calories.toInt()}",
                    label = "Cal",
                    color = NutriOrange,
                    icon = "🔥"
                )
                CompactNutrientBadge(
                    value = "${meal.preparationTime}min",
                    label = "Tiempo",
                    color = NutriBlue,
                    icon = "⏱️"
                )
            }
        }
    }
}

@Composable
private fun CompactNutrientBadge(
    value: String,
    label: String,
    color: Color,
    icon: String
) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = " $label",
                fontSize = 10.sp,
                color = color.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun EmptyMealsState() {
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
            text = "No hay comidas disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = NutriGrayDark,
            textAlign = TextAlign.Center
        )
        Text(
            text = "No se encontraron comidas para este tipo",
            fontSize = 14.sp,
            color = NutriGray,
            textAlign = TextAlign.Center
        )
    }
}

private fun getMealTypeColor(mealType: MealType): Color {
    return when (mealType) {
        MealType.BREAKFAST -> NutriOrange
        MealType.SCHOOL_SNACK -> NutriGreen
        MealType.LUNCH -> NutriBlue
        MealType.AFTERNOON_SNACK -> Color(0xFFFF7043)
        MealType.DINNER -> Color(0xFF6366F1)
        MealType.OPTIONAL_SNACK -> NutriGray
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