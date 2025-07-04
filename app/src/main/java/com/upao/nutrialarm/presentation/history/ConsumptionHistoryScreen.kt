package com.upao.nutrialarm.presentation.history

import androidx.compose.animation.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.MealConsumption
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumptionHistoryScreen(
    onBackPressed: () -> Unit,
    viewModel: ConsumptionHistoryViewModel = hiltViewModel()
) {
    val consumptions by viewModel.consumptions.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val dailySummary by viewModel.dailySummary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Surface(
            color = NutriBlue,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Historial de Consumo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de fecha
            item {
                DateSelectorCard(
                    selectedDate = selectedDate,
                    onDateChanged = { newDate ->
                        viewModel.selectDate(newDate)
                    }
                )
            }

            // Resumen nutricional del día
            item {
                dailySummary?.let { summary ->
                    DailySummaryCard(summary = summary)
                }
            }

            // Lista de consumos del día
            item {
                Text(
                    text = "Comidas Consumidas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NutriBlue)
                    }
                }
            } else if (consumptions.isEmpty()) {
                item {
                    EmptyStateCard()
                }
            } else {
                // Agrupar por tipo de comida - CORREGIDO
                val groupedConsumptions = consumptions.groupBy { it.consumption.mealType }

                items(MealType.values().toList()) { mealType ->
                    val mealsForType = groupedConsumptions[mealType] ?: emptyList()
                    if (mealsForType.isNotEmpty()) {
                        MealTypeSection(
                            mealType = mealType,
                            consumptions = mealsForType.map { it.consumption }, // CORREGIDO
                            mealNames = mealsForType.associate { it.consumption.id to it.mealName }, // NUEVO
                            onDeleteConsumption = { consumption ->
                                viewModel.deleteConsumption(consumption)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSelectorCard(
    selectedDate: String,
    onDateChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                tint = NutriBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fecha seleccionada",
                    fontSize = 14.sp,
                    color = NutriGray
                )
                Text(
                    text = formatDisplayDate(selectedDate),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutriGrayDark
                )
            }

            Row {
                IconButton(
                    onClick = {
                        val newDate = changeDate(selectedDate, -1)
                        onDateChanged(newDate)
                    }
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Día anterior",
                        tint = NutriBlue
                    )
                }

                IconButton(
                    onClick = {
                        val newDate = changeDate(selectedDate, 1)
                        onDateChanged(newDate)
                    }
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Día siguiente",
                        tint = NutriBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun DailySummaryCard(summary: DailySummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Resumen Nutricional",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NutritionSummaryItem(
                    value = "${summary.totalIron}mg",
                    label = "Hierro",
                    color = IronRed,
                    icon = "🩸",
                    modifier = Modifier.weight(1f)
                )
                NutritionSummaryItem(
                    value = "${summary.totalCalories.toInt()}",
                    label = "Calorías",
                    color = NutriOrange,
                    icon = "🔥",
                    modifier = Modifier.weight(1f)
                )
                NutritionSummaryItem(
                    value = "${summary.totalVitaminC}mg",
                    label = "Vit. C",
                    color = NutriGreen,
                    icon = "🍊",
                    modifier = Modifier.weight(1f)
                )
                NutritionSummaryItem(
                    value = "${summary.totalMeals}",
                    label = "Comidas",
                    color = NutriBlue,
                    icon = "🍽️",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NutritionSummaryItem(
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 16.sp)
            Text(
                text = value,
                fontSize = 14.sp,
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
private fun MealTypeSection(
    mealType: MealType,
    consumptions: List<MealConsumption>, // CORREGIDO
    mealNames: Map<String, String>, // NUEVO - mapeo de IDs a nombres
    onDeleteConsumption: (MealConsumption) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header del tipo de comida
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getMealTypeIcon(mealType),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = getMealDisplayName(mealType),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = getMealTypeColor(mealType)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = getMealTypeColor(mealType).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${consumptions.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = getMealTypeColor(mealType),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de consumos para este tipo
            consumptions.forEach { consumption ->
                ConsumptionItem(
                    consumption = consumption,
                    mealName = mealNames[consumption.id] ?: "Comida desconocida", // CORREGIDO
                    onDelete = { onDeleteConsumption(consumption) }
                )
                if (consumption != consumptions.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = NutriGray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ConsumptionItem(
    consumption: MealConsumption,
    mealName: String, // NUEVO
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = mealName, // CAMBIADO de ID a nombre
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = NutriGrayDark
            )
            Text(
                text = "Consumida a las ${formatTime(consumption.consumedAt)}",
                fontSize = 12.sp,
                color = NutriGray
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NutrientBadge(
                    value = "${consumption.ironContent}mg",
                    label = "Fe",
                    color = IronRed
                )
                NutrientBadge(
                    value = "${consumption.calories.toInt()}",
                    label = "Cal",
                    color = NutriOrange
                )
            }
        }

        IconButton(onClick = onDelete) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun NutrientBadge(
    value: String,
    label: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "$label: $value",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
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
                text = "No hay comidas registradas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = NutriGrayDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Las comidas que marques como consumidas aparecerán aquí",
                fontSize = 14.sp,
                color = NutriGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Funciones auxiliares
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

private fun formatDisplayDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

private fun formatTime(timestamp: Long): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(Date(timestamp))
}

private fun changeDate(currentDate: String, days: Int): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(currentDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, days)
        format.format(calendar.time)
    } catch (e: Exception) {
        currentDate
    }
}

// Data class para el resumen diario
data class DailySummary(
    val totalIron: Double,
    val totalCalories: Double,
    val totalVitaminC: Double,
    val totalFolate: Double,
    val totalMeals: Int
)