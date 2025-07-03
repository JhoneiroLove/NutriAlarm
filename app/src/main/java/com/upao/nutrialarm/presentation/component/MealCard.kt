package com.upao.nutrialarm.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upao.nutrialarm.domain.model.Meal
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    showNutritionInfo: Boolean = true,
    onMealClick: (() -> Unit)? = null,
    onRecipeClick: (() -> Unit)? = null,
    onSelectClick: (() -> Unit)? = null
) {
    val mealTypeColor = getMealTypeColor(meal.mealType)
    val backgroundColor = if (isSelected) mealTypeColor.copy(alpha = 0.1f) else Color.White
    val borderColor = if (isSelected) mealTypeColor else Color.Transparent

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onMealClick?.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, borderColor)
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp,
            pressedElevation = 12.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Header con gradiente
            MealCardHeader(
                meal = meal,
                mealTypeColor = mealTypeColor,
                isSelected = isSelected
            )

            // Contenido principal
            MealCardContent(
                meal = meal,
                showNutritionInfo = showNutritionInfo,
                onRecipeClick = onRecipeClick,
                onSelectClick = onSelectClick,
                isSelected = isSelected
            )
        }
    }
}

@Composable
private fun MealCardHeader(
    meal: Meal,
    mealTypeColor: Color,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        mealTypeColor,
                        mealTypeColor.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getMealTypeIcon(meal.mealType),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = getMealDisplayName(meal.mealType),
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun MealCardContent(
    meal: Meal,
    showNutritionInfo: Boolean,
    onRecipeClick: (() -> Unit)?,
    onSelectClick: (() -> Unit)?,
    isSelected: Boolean
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Descripción
        Text(
            text = meal.description,
            fontSize = 14.sp,
            color = NutriGray,
            lineHeight = 20.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (showNutritionInfo) {
            Spacer(modifier = Modifier.height(12.dp))

            // Información nutricional
            NutritionInfoRow(meal = meal)

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Botones de acción
        ActionButtonsRow(
            onRecipeClick = onRecipeClick,
            onSelectClick = onSelectClick,
            isSelected = isSelected
        )
    }
}

@Composable
private fun NutritionInfoRow(meal: Meal) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NutritionBadge(
            value = "${meal.ironContent}mg",
            label = "Hierro",
            color = IronRed,
            icon = "🩸",
            modifier = Modifier.weight(1f)
        )
        NutritionBadge(
            value = "${meal.calories.toInt()}",
            label = "Cal",
            color = NutriOrange,
            icon = "🔥",
            modifier = Modifier.weight(1f)
        )
        NutritionBadge(
            value = "${meal.preparationTime}min",
            label = "Tiempo",
            color = NutriBlue,
            icon = "⏱️",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NutritionBadge(
    value: String,
    label: String,
    color: Color,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 12.sp
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = color.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ActionButtonsRow(
    onRecipeClick: (() -> Unit)?,
    onSelectClick: (() -> Unit)?,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Botón de ver receta
        onRecipeClick?.let {
            OutlinedButton(
                onClick = it,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = NutriBlue
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, NutriBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Receta",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Botón de seleccionar
        onSelectClick?.let {
            Button(
                onClick = it,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) NutriGreen else NutriBlue,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    if (isSelected) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSelected) "Elegido" else "Elegir",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Funciones auxiliares
private fun getMealTypeColor(mealType: MealType): Color {
    return when (mealType) {
        MealType.BREAKFAST -> NutriOrange
        MealType.SCHOOL_SNACK -> NutriGreen
        MealType.LUNCH -> NutriBlue
        MealType.AFTERNOON_SNACK -> Color(0xFFFF7043) // Naranja más suave
        MealType.DINNER -> Color(0xFF6366F1) // Púrpura
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

// ========================================
// Variantes especializadas del MealCard
// ========================================

@Composable
fun CompactMealCard(
    meal: Meal,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del tipo de comida
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        getMealTypeColor(meal.mealType).copy(alpha = 0.2f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getMealTypeIcon(meal.mealType),
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información de la comida
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutriGrayDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${meal.ironContent}mg hierro • ${meal.calories.toInt()} cal",
                    fontSize = 12.sp,
                    color = NutriGray
                )
            }

            // Flecha
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
fun SelectableMealCard(
    meal: Meal,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    MealCard(
        meal = meal,
        modifier = modifier,
        isSelected = isSelected,
        showNutritionInfo = true,
        onMealClick = { onSelectionChanged(!isSelected) },
        onSelectClick = { onSelectionChanged(!isSelected) }
    )
}

@Composable
fun MealPreviewCard(
    meal: Meal,
    onViewRecipe: () -> Unit,
    modifier: Modifier = Modifier
) {
    MealCard(
        meal = meal,
        modifier = modifier,
        showNutritionInfo = true,
        onRecipeClick = onViewRecipe
    )
}