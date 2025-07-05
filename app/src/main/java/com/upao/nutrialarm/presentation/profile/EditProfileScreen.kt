package com.upao.nutrialarm.presentation.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.ActivityLevel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = uiState.user

    // Estados locales para el formulario
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var age by remember { mutableStateOf(currentUser?.age?.toString() ?: "") }
    var weight by remember { mutableStateOf(currentUser?.weight?.toString() ?: "") }
    var height by remember { mutableStateOf(currentUser?.height?.toString() ?: "") }
    var selectedActivityLevel by remember { mutableStateOf(currentUser?.activityLevel ?: ActivityLevel.SEDENTARY) }
    var selectedAnemiaRisk by remember { mutableStateOf(currentUser?.anemiaRisk ?: AnemiaRisk.LOW) }
    var showActivityDropdown by remember { mutableStateOf(false) }
    var showRiskDropdown by remember { mutableStateOf(false) }

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
        delay(200)
        contentVisible = true
    }

    // Actualizar campos cuando se carga el usuario
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            name = user.name
            age = user.age.toString()
            weight = user.weight.toString()
            height = user.height.toString()
            selectedActivityLevel = user.activityLevel
            selectedAnemiaRisk = user.anemiaRisk
        }
    }

    // Manejar actualización exitosa
    LaunchedEffect(uiState.isUpdateSuccessful) {
        if (uiState.isUpdateSuccessful) {
            delay(1500) // Mostrar mensaje de éxito por un momento
            onNavigateBack()
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
                EditProfileHeader(onNavigateBack = onNavigateBack)
            }

            // Contenido
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                EditProfileContent(
                    name = name,
                    onNameChange = { name = it },
                    age = age,
                    onAgeChange = { age = it },
                    weight = weight,
                    onWeightChange = { weight = it },
                    height = height,
                    onHeightChange = { height = it },
                    selectedActivityLevel = selectedActivityLevel,
                    onActivityLevelChange = { selectedActivityLevel = it },
                    selectedAnemiaRisk = selectedAnemiaRisk,
                    onAnemiaRiskChange = { selectedAnemiaRisk = it },
                    showActivityDropdown = showActivityDropdown,
                    onActivityDropdownChange = { showActivityDropdown = it },
                    showRiskDropdown = showRiskDropdown,
                    onRiskDropdownChange = { showRiskDropdown = it },
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    successMessage = if (uiState.isUpdateSuccessful) "¡Datos actualizados correctamente!" else null,
                    onSaveClick = {
                        viewModel.updateUserProfile(
                            name = name,
                            age = age,
                            weight = weight,
                            height = height,
                            activityLevel = selectedActivityLevel,
                            anemiaRisk = selectedAnemiaRisk
                        )
                    }
                )
            }
        }

        // Loading overlay
        if (uiState.isLoading) {
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
                            color = NutriBlue,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Guardando cambios...",
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
private fun EditProfileHeader(onNavigateBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = NutriBlue
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
                            NutriBlue,
                            Color(0xFF60A5FA)
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
                        text = "Datos Personales",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Actualiza tu información",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "✏️",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileContent(
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    weight: String,
    onWeightChange: (String) -> Unit,
    height: String,
    onHeightChange: (String) -> Unit,
    selectedActivityLevel: ActivityLevel,
    onActivityLevelChange: (ActivityLevel) -> Unit,
    selectedAnemiaRisk: AnemiaRisk,
    onAnemiaRiskChange: (AnemiaRisk) -> Unit,
    showActivityDropdown: Boolean,
    onActivityDropdownChange: (Boolean) -> Unit,
    showRiskDropdown: Boolean,
    onRiskDropdownChange: (Boolean) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Información básica
        ProfileSection(title = "Información Básica") {
            ModernTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Nombre completo",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )
        }

        // Datos físicos
        ProfileSection(title = "Datos Físicos") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModernTextField(
                    value = age,
                    onValueChange = onAgeChange,
                    label = "Edad",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                ModernTextField(
                    value = weight,
                    onValueChange = onWeightChange,
                    label = "Peso (kg)",
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )

                ModernTextField(
                    value = height,
                    onValueChange = onHeightChange,
                    label = "Altura (cm)",
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Configuración de salud
        ProfileSection(title = "Configuración de Salud") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ModernDropdown(
                    value = getActivityLevelText(selectedActivityLevel),
                    onValueChange = onActivityLevelChange,
                    options = ActivityLevel.values().toList(),
                    optionText = { getActivityLevelText(it) },
                    label = "Nivel de Actividad",
                    expanded = showActivityDropdown,
                    onExpandedChange = onActivityDropdownChange,
                    leadingIcon = Icons.Default.DirectionsRun
                )

                ModernDropdown(
                    value = getAnemiaRiskText(selectedAnemiaRisk),
                    onValueChange = onAnemiaRiskChange,
                    options = AnemiaRisk.values().toList(),
                    optionText = { getAnemiaRiskText(it) },
                    label = "Riesgo de Anemia",
                    expanded = showRiskDropdown,
                    onExpandedChange = onRiskDropdownChange,
                    leadingIcon = Icons.Default.Favorite
                )
            }
        }

        // Mensajes
        successMessage?.let {
            MessageCard(message = it, isError = false)
        }

        errorMessage?.let {
            MessageCard(message = it, isError = true)
        }

        // Botón guardar
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = NutriGreen,
                disabledContainerColor = NutriGray
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Guardar Cambios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NutriGrayDark,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    it,
                    contentDescription = null,
                    tint = NutriBlue
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NutriBlue,
            focusedLabelColor = NutriBlue,
            cursorColor = NutriBlue,
            focusedTextColor = NutriGrayDark,
            unfocusedTextColor = NutriGrayDark
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> ModernDropdown(
    value: String,
    onValueChange: (T) -> Unit,
    options: List<T>,
    optionText: (T) -> String,
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = NutriBlue
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NutriBlue,
                focusedLabelColor = NutriBlue,
                focusedTextColor = NutriGrayDark,
                unfocusedTextColor = NutriGrayDark
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionText(option)) },
                    onClick = {
                        onValueChange(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
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

private fun getActivityLevelText(level: ActivityLevel): String {
    return when (level) {
        ActivityLevel.SEDENTARY -> "Sedentario"
        ActivityLevel.LIGHT -> "Ligero"
        ActivityLevel.MODERATE -> "Moderado"
        ActivityLevel.ACTIVE -> "Activo"
        ActivityLevel.VERY_ACTIVE -> "Muy Activo"
    }
}

private fun getAnemiaRiskText(risk: AnemiaRisk): String {
    return when (risk) {
        AnemiaRisk.LOW -> "Riesgo Bajo"
        AnemiaRisk.MEDIUM -> "Riesgo Medio"
        AnemiaRisk.HIGH -> "Riesgo Alto"
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    NutriAlarmTheme {
        EditProfileScreen()
    }
}