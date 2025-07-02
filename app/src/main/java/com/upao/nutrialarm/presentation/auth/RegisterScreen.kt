package com.upao.nutrialarm.presentation.auth

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.ActivityLevel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedActivityLevel by remember { mutableStateOf(ActivityLevel.SEDENTARY) }
    var selectedAnemiaRisk by remember { mutableStateOf(AnemiaRisk.LOW) }
    var showActivityDropdown by remember { mutableStateOf(false) }
    var showRiskDropdown by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Animaciones
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        contentVisible = true
    }

    // Observar el estado de registro exitoso
    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            onNavigateToHome()
            viewModel.resetRegistrationState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A), // Azul profundo
                        Color(0xFF3B82F6), // Azul medio
                        Color(0xFF60A5FA), // Azul claro
                        Color.White
                    ),
                    startY = 0f,
                    endY = screenHeight.value * 1.5f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar moderno
            ModernTopBar(onNavigateBack = onNavigateBack)

            // Contenido principal
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                RegisterContent(
                    email = email,
                    onEmailChange = {
                        email = it
                        viewModel.clearError()
                    },
                    password = password,
                    onPasswordChange = {
                        password = it
                        viewModel.clearError()
                    },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = {
                        confirmPassword = it
                        viewModel.clearError()
                    },
                    name = name,
                    onNameChange = {
                        name = it
                        viewModel.clearError()
                    },
                    age = age,
                    onAgeChange = {
                        age = it
                        viewModel.clearError()
                    },
                    weight = weight,
                    onWeightChange = {
                        weight = it
                        viewModel.clearError()
                    },
                    height = height,
                    onHeightChange = {
                        height = it
                        viewModel.clearError()
                    },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                    confirmPasswordVisible = confirmPasswordVisible,
                    onConfirmPasswordVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
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
                    onRegisterClick = {
                        viewModel.register(
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernTopBar(onNavigateBack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        NutriBlue.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = NutriBlue
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
                Text(
                    text = "Únete a NutriAlarm",
                    fontSize = 14.sp,
                    color = NutriGray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    weight: String,
    onWeightChange: (String) -> Unit,
    height: String,
    onHeightChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChange: () -> Unit,
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
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo pequeño
        Spacer(modifier = Modifier.height(24.dp)) // Más espacio desde el top
        Text(text = "🍎", fontSize = 40.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Sección: Información Personal
                SectionHeader(
                    title = "Información Personal",
                    icon = Icons.Default.Person
                )

                ModernTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = "Nombre completo",
                    leadingIcon = Icons.Default.Person,
                    keyboardType = KeyboardType.Text
                )

                ModernTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Correo electrónico",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                ModernTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = onPasswordVisibilityChange
                )

                ModernTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirmar contraseña",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = confirmPasswordVisible,
                    onPasswordVisibilityToggle = onConfirmPasswordVisibilityChange
                )

                // Sección: Datos Físicos
                SectionHeader(
                    title = "Datos Físicos",
                    icon = Icons.Default.FitnessCenter
                )

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

                // Dropdowns modernos
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

                // Error message
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    errorMessage?.let {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = NutriRed.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                color = NutriRed,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Botón de registro
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriBlue,
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
                        Text(
                            "Crear Cuenta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = NutriBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NutriGrayDark
        )
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
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null
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
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onPasswordVisibilityToggle?.invoke() }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = NutriGray
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
fun RegisterScreenPreview() {
    NutriAlarmTheme {
        RegisterScreen()
    }
}