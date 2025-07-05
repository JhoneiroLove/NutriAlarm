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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.presentation.component.BannerAdView
import com.upao.nutrialarm.presentation.admob.rememberAdMobHelper
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
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToHealthStatus: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()

    // AdMob integration
    val adMobHelper = rememberAdMobHelper()
    val context = LocalContext.current

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
                    adMobHelper = adMobHelper,
                    onNavigateToEditProfile = {
                        onNavigateToEditProfile()
                        // Mostrar intersticial al navegar
                        if (context is android.app.Activity) {
                            adMobHelper?.tryShowInterstitialAd(context)
                        }
                    },
                    onNavigateToHealthStatus = {
                        onNavigateToHealthStatus()
                        // Mostrar intersticial al navegar
                        if (context is android.app.Activity) {
                            adMobHelper?.tryShowInterstitialAd(context)
                        }
                    },
                    onNavigateToAlarms = {
                        onNavigateToAlarms()
                        // Mostrar intersticial al navegar
                        if (context is android.app.Activity) {
                            adMobHelper?.tryShowInterstitialAd(context)
                        }
                    },
                    onNavigateToMealSelection = {
                        onNavigateToMealSelection()
                        // Mostrar intersticial al navegar
                        if (context is android.app.Activity) {
                            adMobHelper?.tryShowInterstitialAd(context)
                        }
                    },
                    onNavigateToSettings = {
                        onNavigateToSettings()
                        // Mostrar intersticial al navegar
                        if (context is android.app.Activity) {
                            adMobHelper?.tryShowInterstitialAd(context)
                        }
                    },
                    onLogout = {
                        // Mostrar intersticial antes del logout
                        if (context is android.app.Activity) {
                            adMobHelper?.tryShowInterstitialAd(context) {
                                onLogout()
                            }
                        } else {
                            onLogout()
                        }
                    }
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
    adMobHelper: com.upao.nutrialarm.presentation.admob.AdMobIntegrationHelper?,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToHealthStatus: () -> Unit,
    onNavigateToAlarms: () -> Unit,
    onNavigateToMealSelection: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
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
            BannerAdView(
                modifier = Modifier.padding(vertical = 8.dp),
                adMobService = adMobHelper?.getAdMobService()
            )
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
                onClick = onNavigateToEditProfile
            )
        }

        item {
            ProfileMenuItem(
                title = "Seleccionar Menús",
                subtitle = "Personaliza qué quieres comer en cada horario",
                icon = Icons.Default.RestaurantMenu,
                iconColor = NutriGreen,
                onClick = onNavigateToMealSelection
            )
        }

        item {
            ProfileMenuItem(
                title = "Configurar Alarmas",
                subtitle = "Horarios de comidas y recordatorios",
                icon = Icons.Default.Notifications,
                iconColor = NutriOrange,
                onClick = onNavigateToAlarms
            )
        }

        // Banner Ad en el medio de los elementos del menú
        item {
            BannerAdView(
                modifier = Modifier.padding(vertical = 8.dp),
                adMobService = adMobHelper?.getAdMobService()
            )
        }

        item {
            ProfileMenuItem(
                title = "Estado de Salud",
                subtitle = "Nivel de riesgo de anemia y evaluación nutricional",
                icon = Icons.Default.Favorite,
                iconColor = NutriRed,
                onClick = onNavigateToHealthStatus
            )
        }

        item {
            ProfileMenuItem(
                title = "Configuración",
                subtitle = "Notificaciones, tema y configuración de la app",
                icon = Icons.Default.Settings,
                iconColor = NutriGray,
                onClick = onNavigateToSettings
            )
        }

        if (adMobHelper?.isRewardedAdAvailable() == true) {
            item {
                RewardedAdProfileCard(
                    onWatchAd = {
                        // lógica del anuncio con recompensa
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            LogoutButton(onLogout = onLogout)
        }

        // Banner Ad final
        item {
            BannerAdView(
                modifier = Modifier.padding(vertical = 16.dp),
                adMobService = adMobHelper?.getAdMobService()
            )
        }
    }
}

@Composable
private fun RewardedAdProfileCard(
    onWatchAd: () -> Unit
) {
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
                            NutriOrange.copy(alpha = 0.1f),
                            NutriRed.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            NutriOrange.copy(alpha = 0.2f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Funciones Premium",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = "Mira un video y desbloquea características especiales",
                        fontSize = 14.sp,
                        color = NutriGray
                    )
                }

                Button(
                    onClick = onWatchAd,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NutriOrange
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Ver",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
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
private fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
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