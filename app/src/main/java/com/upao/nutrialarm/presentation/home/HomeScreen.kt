package com.upao.nutrialarm.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upao.nutrialarm.domain.model.MealType
import com.upao.nutrialarm.presentation.profile.UserProfileViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.ui.theme.IronRed
import com.upao.nutrialarm.ui.theme.NutriAlarmTheme
import com.upao.nutrialarm.ui.theme.NutriBlue
import com.upao.nutrialarm.ui.theme.NutriGray
import com.upao.nutrialarm.ui.theme.NutriGrayDark
import com.upao.nutrialarm.ui.theme.NutriGreen
import com.upao.nutrialarm.ui.theme.NutriOrange
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDiets: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentTime = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
    val nextMeal = remember { MealType.AFTERNOON_SNACK }

    // Animaciones
    var headerVisible by remember { mutableStateOf(false) }
    var nextMealVisible by remember { mutableStateOf(false) }
    var statsVisible by remember { mutableStateOf(false) }
    var menuVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
        delay(200)
        nextMealVisible = true
        delay(200)
        statsVisible = true
        delay(200)
        menuVisible = true
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp), // Más padding top
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = headerVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { -it / 2 }
                    )
                ) {
                    HeaderSection(currentUser = currentUser)
                }
            }

            item {
                AnimatedVisibility(
                    visible = nextMealVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInHorizontally(
                        animationSpec = tween(600),
                        initialOffsetX = { -it / 2 }
                    )
                ) {
                    NextMealCard(nextMeal = nextMeal)
                }
            }

            item {
                AnimatedVisibility(
                    visible = statsVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { it / 2 }
                    )
                ) {
                    DailyProgressSection()
                }
            }

            item {
                AnimatedVisibility(
                    visible = menuVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { it / 2 }
                    )
                ) {
                    QuickActionsSection(
                        onNavigateToDiets = onNavigateToDiets,
                        onNavigateToProfile = onNavigateToProfile
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = menuVisible,
                    enter = fadeIn(animationSpec = tween(800))
                ) {
                    TipsSection()
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(currentUser: User?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NutriBlue
        ),
        shape = RoundedCornerShape(20.dp),
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
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "¡Hola!",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = currentUser?.name ?: "Usuario",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "🍎",
                        fontSize = 40.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Alimentación saludable contra la anemia",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NextMealCard(nextMeal: MealType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NutriGreen
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Próxima comida",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = getMealDisplayName(nextMeal),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "17:00 - 17:30",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun DailyProgressSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Progreso de Hoy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriGrayDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    listOf(
                        ProgressItem("Hierro", "8.5", "18", "mg", IronRed),
                        ProgressItem("Calorías", "1,420", "2,000", "cal", NutriOrange),
                        ProgressItem("Comidas", "3", "6", "", NutriBlue),
                        ProgressItem("Agua", "6", "8", "vasos", Color(0xFF06B6D4))
                    )
                ) { item ->
                    ProgressCard(item)
                }
            }
        }
    }
}

@Composable
private fun ProgressCard(item: ProgressItem) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.label,
                fontSize = 12.sp,
                color = item.color,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = item.current,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = item.color
            )
            Text(
                text = "de ${item.target} ${item.unit}",
                fontSize = 10.sp,
                color = item.color.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickActionsSection(
    onNavigateToDiets: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column {
        Text(
            text = "Acciones Rápidas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NutriGrayDark,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                modifier = Modifier.weight(1f),
                title = "Ver Dietas",
                subtitle = "Dietas personalizadas",
                icon = Icons.Default.Restaurant,
                color = NutriBlue,
                onClick = onNavigateToDiets
            )

            ActionCard(
                modifier = Modifier.weight(1f),
                title = "Mi Perfil",
                subtitle = "Configurar alarmas",
                icon = Icons.Default.Person,
                color = NutriGreen,
                onClick = onNavigateToProfile
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(120.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color.copy(alpha = 0.1f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriGrayDark
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = NutriGray
                    )
                }
            }
        }
    }
}

@Composable
private fun TipsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = NutriOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Consejo del Día",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Combina alimentos ricos en hierro con frutas cítricas para mejorar su absorción. Por ejemplo, lentejas con naranja o espinacas con limón.",
                fontSize = 14.sp,
                color = NutriGray,
                lineHeight = 20.sp
            )
        }
    }
}

// Data classes para el progreso
private data class ProgressItem(
    val label: String,
    val current: String,
    val target: String,
    val unit: String,
    val color: Color
)

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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NutriAlarmTheme {
        HomeScreen()
    }
}