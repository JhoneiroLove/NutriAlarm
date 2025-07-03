package com.upao.nutrialarm.presentation.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upao.nutrialarm.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    // Estados locales simples para la demo
    var notificationsEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var selectedTheme by remember { mutableStateOf("Sistema") }
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
        delay(200)
        contentVisible = true
    }

    // Limpiar mensaje después de unos segundos
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            delay(3000)
            message = ""
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
                SettingsHeader(onNavigateBack = onNavigateBack)
            }

            // Contenido
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        SettingsSection(title = "Notificaciones") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ToggleSettingItem(
                                    title = "Recordatorios de comida",
                                    subtitle = "Recibe notificaciones para tus horarios de comida",
                                    icon = Icons.Default.Notifications,
                                    isChecked = notificationsEnabled,
                                    onToggle = {
                                        notificationsEnabled = it
                                        if (!it) vibrationEnabled = false
                                        message = if (it) "Notificaciones activadas" else "Notificaciones desactivadas"
                                        isError = false
                                    }
                                )

                                ToggleSettingItem(
                                    title = "Vibración",
                                    subtitle = "Vibrar cuando lleguen las notificaciones",
                                    icon = Icons.Default.Vibration,
                                    isChecked = vibrationEnabled,
                                    onToggle = {
                                        vibrationEnabled = it
                                        message = if (it) "Vibración activada" else "Vibración desactivada"
                                        isError = false
                                    },
                                    enabled = notificationsEnabled
                                )
                            }
                        }
                    }

                    item {
                        SettingsSection(title = "Apariencia") {
                            ThemeSelector(
                                currentTheme = selectedTheme,
                                onThemeChanged = {
                                    selectedTheme = it
                                    message = "Tema cambiado a $it"
                                    isError = false
                                }
                            )
                        }
                    }

                    item {
                        SettingsSection(title = "Datos y Almacenamiento") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ActionSettingItem(
                                    title = "Limpiar caché",
                                    subtitle = "Libera espacio eliminando archivos temporales",
                                    icon = Icons.Default.CleaningServices,
                                    onClick = {
                                        message = "Caché limpiado exitosamente"
                                        isError = false
                                    }
                                )

                                ActionSettingItem(
                                    title = "Exportar datos",
                                    subtitle = "Crea una copia de seguridad de tus datos",
                                    icon = Icons.Default.Download,
                                    onClick = {
                                        message = "Datos exportados exitosamente"
                                        isError = false
                                    }
                                )
                            }
                        }
                    }

                    item {
                        SettingsSection(title = "Avanzado") {
                            ActionSettingItem(
                                title = "Restablecer aplicación",
                                subtitle = "Elimina todos los datos y vuelve a la configuración inicial",
                                icon = Icons.Default.RestartAlt,
                                onClick = {
                                    message = "Aplicación restablecida. Reinicia la app para aplicar los cambios."
                                    isError = false
                                },
                                isDangerous = true
                            )
                        }
                    }

                    if (message.isNotEmpty()) {
                        item {
                            MessageCard(
                                message = message,
                                isError = isError
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsHeader(onNavigateBack: () -> Unit) {
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
                        text = "Configuración",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Personaliza tu experiencia",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "⚙️",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
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
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ToggleSettingItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) NutriBlue else NutriGray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (enabled) NutriGrayDark else NutriGray
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = NutriGray,
                lineHeight = 18.sp
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NutriGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = NutriGray
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionSettingItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDangerous: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isDangerous) NutriRed.copy(alpha = 0.05f) else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDangerous) NutriRed else NutriBlue,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDangerous) NutriRed else NutriGrayDark
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = NutriGray,
                    lineHeight = 18.sp
                )
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
private fun ThemeSelector(
    currentTheme: String,
    onThemeChanged: (String) -> Unit
) {
    val themes = listOf("Claro", "Oscuro", "Sistema")

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Palette,
                contentDescription = null,
                tint = NutriBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Tema de la aplicación",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = NutriGrayDark
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        themes.forEach { theme ->
            ThemeOption(
                theme = theme,
                isSelected = currentTheme == theme,
                onSelected = { onThemeChanged(theme) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeOption(
    theme: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelected,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) NutriBlue.copy(alpha = 0.1f) else Color.Transparent
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, NutriBlue)
        } else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = NutriBlue,
                    unselectedColor = NutriGray
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = theme,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = NutriGrayDark
                )
                Text(
                    text = getThemeDescription(theme),
                    fontSize = 14.sp,
                    color = NutriGray
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

private fun getThemeDescription(theme: String): String {
    return when (theme) {
        "Claro" -> "Tema claro siempre activo"
        "Oscuro" -> "Tema oscuro siempre activo"
        "Sistema" -> "Sigue la configuración del sistema"
        else -> ""
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    NutriAlarmTheme {
        SettingsScreen()
    }
}