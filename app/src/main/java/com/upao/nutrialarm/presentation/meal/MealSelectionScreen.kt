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
fun MealSelectionScreen(
    onNavigateBack: () -> Unit = {},
    onMealRecipeClick: (String) -> Unit = {},
    viewModel: MealSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedMealType by viewModel.selectedMealType.collectAsState()

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUserPreferences("current_user") // TODO: Get real user ID
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
            // Header moderno
            AnimatedVisibility(
                visible = headerVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { -it / 2 }
                )
            ) {
                MealSelectionHeader(onNavigateBack = onNavigateBack)
            }

            // Contenido principal
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                MealSelectionContent(
                    uiState = uiState,
                    selectedMealType = selectedMealType,
                    onMealTypeSelected = viewModel::selectMealType,
                    onMealSelected = viewModel::selectMeal,
                    onTimeChanged = viewModel::updateMealTime,
                    onReminderToggled = viewModel::toggleReminder,
                    onMealRecipeClick = onMealRecipeClick
                )
            }
        }
    }
}

@Composable
private fun MealSelectionHeader(onNavigateBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = NutriGreen
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
                            NutriGreen,
                            Color(0xFF4ADE80)
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
                        text = "Seleccionar Menús",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Personaliza tus comidas y horarios",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "🍽️",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
private fun MealSelectionContent(
    uiState: MealSelectionUiState,
    selectedMealType: MealType?,
    onMealTypeSelected: (MealType) -> Unit,
    onMealSelected: (MealType, String) -> Unit,
    onTimeChanged: (MealType, String) -> Unit,
    onReminderToggled: (MealType, Boolean) -> Unit,
    onMealRecipeClick: (String) -> Unit
) {
    when {
        uiState.isLoading -> {
            LoadingContent()
        }
        uiState.error != null -> {
            ErrorContent(error = uiState.error)
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    InfoCard()
                }

                // ✅ PESTAÑAS CORREGIDAS - Sin LazyColumn anidado
                item {
                    MealTypeTabsCard(
                        selectedMealType = selectedMealType,
                        onMealTypeSelected = onMealTypeSelected
                    )
                }

                // Mostrar opciones para el tipo de comida seleccionado
                selectedMealType?.let { mealType ->
                    item {
                        MealTypeSection(
                            mealType = mealType,
                            userPreference = uiState.userPreferences[mealType],
                            availableMeals = uiState.mealsByType[mealType] ?: emptyList(),
                            onMealSelected = { mealId -> onMealSelected(mealType, mealId) },
                            onTimeChanged = { time -> onTimeChanged(mealType, time) },
                            onReminderToggled = { enabled -> onReminderToggled(mealType, enabled) },
                            onMealRecipeClick = onMealRecipeClick
                        )
                    }
                }

                if (uiState.message.isNotEmpty()) {
                    item {
                        MessageCard(message = uiState.message, isError = uiState.isError)
                    }
                }
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
                color = NutriGreen,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Cargando opciones...",
                color = NutriGray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorContent(error: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
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
        }
    }
}

@Composable
private fun InfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NutriBlue.copy(alpha = 0.1f),
                            NutriGreen.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            NutriGreen.copy(alpha = 0.2f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.RestaurantMenu,
                        contentDescription = null,
                        tint = NutriGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Menús Personalizados",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = "Elige qué quieres comer en cada horario y recibe recordatorios específicos",
                        fontSize = 14.sp,
                        color = NutriGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

// ✅ CORRECCIÓN PRINCIPAL: Cambiar LazyColumn por Column normal
@Composable
private fun MealTypeTabsCard(
    selectedMealType: MealType?,
    onMealTypeSelected: (MealType) -> Unit
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
            Text(
                text = "Selecciona el tipo de comida",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // ✅ CAMBIO CRÍTICO: Column en lugar de LazyColumn
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MealType.values().forEachIndexed { index, mealType ->
                    MealTypeTab(
                        mealType = mealType,
                        isSelected = selectedMealType == mealType,
                        onClick = { onMealTypeSelected(mealType) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealTypeTab(
    mealType: MealType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) NutriGreen.copy(alpha = 0.1f) else Color.Transparent
    val borderColor = if (isSelected) NutriGreen else NutriGray.copy(alpha = 0.3f)
    val textColor = if (isSelected) NutriGreen else NutriGrayDark

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getMealTypeIcon(mealType),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = getMealDisplayName(mealType),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                Text(
                    text = mealType.getRecommendedTimeRange(),
                    fontSize = 12.sp,
                    color = NutriGray
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NutriGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = NutriGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun MealTypeSection(
    mealType: MealType,
    userPreference: UserMealPreference?,
    availableMeals: List<Meal>,
    onMealSelected: (String) -> Unit,
    onTimeChanged: (String) -> Unit,
    onReminderToggled: (Boolean) -> Unit,
    onMealRecipeClick: (String) -> Unit
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
            modifier = Modifier.padding(20.dp)
        ) {
            // Header de la sección
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = getMealTypeIcon(mealType),
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = getMealDisplayName(mealType),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = mealType.getRecommendedTimeRange(),
                        fontSize = 14.sp,
                        color = NutriGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Configuración de horario y recordatorio
            MealTimeAndReminderSection(
                userPreference = userPreference,
                defaultTime = mealType.getDefaultTime(),
                onTimeChanged = onTimeChanged,
                onReminderToggled = onReminderToggled
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de opciones de comida
            Text(
                text = "Opciones de menú:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (availableMeals.isEmpty()) {
                EmptyMealsMessage()
            } else {
                availableMeals.forEach { meal ->
                    MealOptionCard(
                        meal = meal,
                        isSelected = userPreference?.selectedMealId == meal.id,
                        onSelected = { onMealSelected(meal.id) },
                        onRecipeClick = { onMealRecipeClick(meal.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealTimeAndReminderSection(
    userPreference: UserMealPreference?,
    defaultTime: String,
    onTimeChanged: (String) -> Unit,
    onReminderToggled: (Boolean) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val currentTime = userPreference?.timeSlot ?: defaultTime
    val reminderEnabled = userPreference?.reminderEnabled ?: true

    Card(
        colors = CardDefaults.cardColors(
            containerColor = NutriBlue.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Configuración de hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hora de recordatorio",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = NutriGrayDark
                    )
                    Text(
                        text = currentTime,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriBlue
                    )
                }

                TextButton(
                    onClick = { showTimePicker = true }
                ) {
                    Text("Cambiar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Switch de recordatorio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Recordatorio activo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = NutriGrayDark
                    )
                    Text(
                        text = if (reminderEnabled) "Recibirás notificaciones" else "Sin notificaciones",
                        fontSize = 12.sp,
                        color = NutriGray
                    )
                }

                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = onReminderToggled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = NutriGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = NutriGray
                    )
                )
            }
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = currentTime.split(":")[0].toInt(),
            initialMinute = currentTime.split(":")[1].toInt()
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Seleccionar Hora") },
            text = { TimePicker(state = timePickerState) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                        onTimeChanged(newTime)
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealOptionCard(
    meal: Meal,
    isSelected: Boolean,
    onSelected: () -> Unit,
    onRecipeClick: () -> Unit
) {
    val backgroundColor = if (isSelected) NutriGreen.copy(alpha = 0.1f) else Color.Transparent
    val borderColor = if (isSelected) NutriGreen else NutriGray.copy(alpha = 0.3f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Radio button
                RadioButton(
                    selected = isSelected,
                    onClick = onSelected,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = NutriGreen,
                        unselectedColor = NutriGray
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Información de la comida
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meal.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = meal.description,
                        fontSize = 14.sp,
                        color = NutriGray,
                        maxLines = 2
                    )
                }

                // Botón de ver receta
                IconButton(
                    onClick = onRecipeClick
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = "Ver receta",
                        tint = NutriBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(12.dp))

                // Información nutricional resumida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NutrientBadge(
                        label = "Hierro",
                        value = "${meal.ironContent}mg",
                        color = IronRed,
                        icon = "🩸"
                    )
                    NutrientBadge(
                        label = "Calorías",
                        value = "${meal.calories.toInt()}",
                        color = NutriOrange,
                        icon = "🔥"
                    )
                    NutrientBadge(
                        label = "Tiempo",
                        value = "${meal.preparationTime}min",
                        color = NutriBlue,
                        icon = "⏱️"
                    )
                }
            }
        }
    }
}

@Composable
private fun NutrientBadge(
    label: String,
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = icon,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@Composable
private fun EmptyMealsMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🍽️",
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No hay opciones disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = NutriGrayDark
        )
        Text(
            text = "Pronto agregaremos más opciones",
            fontSize = 14.sp,
            color = NutriGray
        )
    }
}

@Composable
private fun MessageCard(message: String, isError: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) NutriRed.copy(alpha = 0.1f) else NutriGreen.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isError) NutriRed else NutriGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = if (isError) NutriRed else NutriGreen,
                fontSize = 14.sp
            )
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

@Preview(showBackground = true)
@Composable
fun MealSelectionScreenPreview() {
    NutriAlarmTheme {
        MealSelectionScreen()
    }
}