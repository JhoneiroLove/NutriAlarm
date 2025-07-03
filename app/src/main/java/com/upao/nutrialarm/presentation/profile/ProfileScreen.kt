package com.upao.nutrialarm.presentation.profile

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.ui.theme.NutriAlarmTheme
import com.upao.nutrialarm.ui.theme.NutriBlue
import com.upao.nutrialarm.ui.theme.NutriGray
import com.upao.nutrialarm.ui.theme.NutriGrayDark
import com.upao.nutrialarm.ui.theme.NutriGreen
import com.upao.nutrialarm.ui.theme.NutriOrange
import com.upao.nutrialarm.ui.theme.NutriRed
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAlarms: () -> Unit = {},
    onNavigateToMealSelection: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
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
                ModernProfileHeader(onNavigateBack = onNavigateBack)
            }

            // Contenido principal
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { it / 3 }
                )
            ) {
                ProfileContent(
                    currentUser = currentUser,
                    onNavigateToAlarms = onNavigateToAlarms,
                    onNavigateToMealSelection = onNavigateToMealSelection,
                    onNavigateToSettings = onNavigateToSettings
                )
            }
        }
    }
}

@Composable
private fun ModernProfileHeader(onNavigateBack: () -> Unit) {
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
                        text = "Mi Perfil",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Configuración y preferencias",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "👤",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    currentUser: User?,
    onNavigateToAlarms: () -> Unit,
    onNavigateToMealSelection: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            UserInfoCard(currentUser = currentUser)
        }

        item {
            Text(
                text = "Configuración",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
            )
        }

        item {
            ProfileMenuItem(
                title = "Datos Personales",
                subtitle = "Edad, peso, altura, nivel de actividad",
                icon = Icons.Default.Person,
                iconColor = NutriBlue,
                onClick = {
                    // TODO: Navegar a editar perfil
                }
            )
        }

        item {
            ProfileMenuItem(
                title = "Seleccionar Menús",
                subtitle = "Personaliza qué quieres comer en cada horario",
                icon = Icons.Default.RestaurantMenu,
                iconColor = NutriGreen,
                onClick = {
                    onNavigateToMealSelection()
                }
            )
        }

        item {
            ProfileMenuItem(
                title = "Configurar Alarmas",
                subtitle = "Horarios de comidas y recordatorios",
                icon = Icons.Default.Notifications,
                iconColor = NutriOrange,
                onClick = {
                    onNavigateToAlarms()
                }
            )
        }

        item {
            ProfileMenuItem(
                title = "Estado de Salud",
                subtitle = "Nivel de riesgo de anemia",
                icon = Icons.Default.Favorite,
                iconColor = NutriRed,
                onClick = {
                    // TODO: Navegar a estado de salud
                }
            )
        }

        item {
            ProfileMenuItem(
                title = "Configuración",
                subtitle = "Notificaciones, tema y configuración de la app",
                icon = Icons.Default.Settings,
                iconColor = NutriGray,
                onClick = {
                    onNavigateToSettings()
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            LogoutButton()
        }
    }
}

@Composable
private fun UserInfoCard(currentUser: User?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NutriBlue.copy(alpha = 0.1f),
                            NutriBlue.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    NutriBlue.copy(alpha = 0.2f),
                                    NutriBlue.copy(alpha = 0.1f)
                                )
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = NutriBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentUser?.name ?: "Usuario",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )

                Text(
                    text = currentUser?.email ?: "usuario@email.com",
                    fontSize = 14.sp,
                    color = NutriGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats rápidas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    UserStat(
                        label = "Días activo",
                        value = "15",
                        icon = "📅"
                    )
                    UserStat(
                        label = "Comidas",
                        value = "45",
                        icon = "🍽️"
                    )
                    UserStat(
                        label = "Hierro avg",
                        value = "12mg",
                        icon = "🩸"
                    )
                }
            }
        }
    }
}

@Composable
private fun UserStat(
    label: String,
    value: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 20.sp
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NutriGrayDark
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = NutriGray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        iconColor.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NutriGrayDark
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = NutriGray
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
private fun LogoutButton() {
    Button(
        onClick = {
            // TODO: Implementar logout
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = NutriRed
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        )
    ) {
        Icon(
            Icons.Default.ExitToApp,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Cerrar Sesión",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NutriAlarmTheme {
        ProfileScreen()
    }
}