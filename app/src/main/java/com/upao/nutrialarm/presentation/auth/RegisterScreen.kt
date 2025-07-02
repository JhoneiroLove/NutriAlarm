package com.upao.nutrialarm.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
                        NutriBlue.copy(alpha = 0.1f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NutriBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo pequeño
                Text(text = "🍎", fontSize = 32.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Únete a NutriAlarm",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriBlue,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Crea tu perfil personalizado contra la anemia",
                    fontSize = 14.sp,
                    color = NutriGrayDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Formulario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Información personal
                        Text(
                            text = "Información Personal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutriGrayDark
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                viewModel.clearError()
                            },
                            label = { Text("Nombre completo") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = NutriBlue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                viewModel.clearError()
                            },
                            label = { Text("Correo electrónico") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = NutriBlue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )

                        // Contraseñas
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                viewModel.clearError()
                            },
                            label = { Text("Contraseña") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NutriBlue)
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (passwordVisible)
                                                android.R.drawable.ic_menu_view
                                            else
                                                android.R.drawable.ic_menu_close_clear_cancel
                                        ),
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                viewModel.clearError()
                            },
                            label = { Text("Confirmar contraseña") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NutriBlue)
                            },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (confirmPasswordVisible)
                                                android.R.drawable.ic_menu_view
                                            else
                                                android.R.drawable.ic_menu_close_clear_cancel
                                        ),
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Datos físicos
                        Text(
                            text = "Datos Físicos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutriGrayDark
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = age,
                                onValueChange = {
                                    age = it
                                    viewModel.clearError()
                                },
                                label = { Text("Edad") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = weight,
                                onValueChange = {
                                    weight = it
                                    viewModel.clearError()
                                },
                                label = { Text("Peso (kg)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = height,
                                onValueChange = {
                                    height = it
                                    viewModel.clearError()
                                },
                                label = { Text("Altura (cm)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true
                            )
                        }

                        // Nivel de actividad
                        ExposedDropdownMenuBox(
                            expanded = showActivityDropdown,
                            onExpandedChange = { showActivityDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = getActivityLevelText(selectedActivityLevel),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Nivel de Actividad") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showActivityDropdown) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = showActivityDropdown,
                                onDismissRequest = { showActivityDropdown = false }
                            ) {
                                ActivityLevel.values().forEach { level ->
                                    DropdownMenuItem(
                                        text = { Text(getActivityLevelText(level)) },
                                        onClick = {
                                            selectedActivityLevel = level
                                            showActivityDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Riesgo de anemia
                        ExposedDropdownMenuBox(
                            expanded = showRiskDropdown,
                            onExpandedChange = { showRiskDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = getAnemiaRiskText(selectedAnemiaRisk),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Riesgo de Anemia") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRiskDropdown) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = showRiskDropdown,
                                onDismissRequest = { showRiskDropdown = false }
                            ) {
                                AnemiaRisk.values().forEach { risk ->
                                    DropdownMenuItem(
                                        text = { Text(getAnemiaRiskText(risk)) },
                                        onClick = {
                                            selectedAnemiaRisk = risk
                                            showRiskDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Error
                        uiState.errorMessage?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                        }

                        // Register button
                        Button(
                            onClick = {
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
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = NutriBlue)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Crear Cuenta")
                            }
                        }
                    }
                }
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