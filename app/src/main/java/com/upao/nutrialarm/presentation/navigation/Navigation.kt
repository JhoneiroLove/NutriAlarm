package com.upao.nutrialarm.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upao.nutrialarm.presentation.auth.LoginScreen
import com.upao.nutrialarm.presentation.auth.RegisterScreen
import com.upao.nutrialarm.presentation.diet.DietListScreen
import com.upao.nutrialarm.presentation.diet.DietDetailScreen
import com.upao.nutrialarm.presentation.meal.MealRecipeScreen
import com.upao.nutrialarm.presentation.meal.MealSelectionScreen
import com.upao.nutrialarm.presentation.home.DynamicHomeScreen
import com.upao.nutrialarm.presentation.profile.ProfileScreen
import com.upao.nutrialarm.presentation.alarm.AlarmConfigScreen
import com.upao.nutrialarm.presentation.settings.SettingsScreen
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    // Observar el estado de autenticación
    val authState by sessionViewModel.authState.collectAsState()

    // Verificar sesión al iniciar
    LaunchedEffect(Unit) {
        sessionViewModel.checkAuthState()
    }

    // Determinar la ruta inicial basada en el estado de autenticación
    val startDestination = when (authState) {
        is AuthState.Authenticated -> "home"
        is AuthState.Unauthenticated -> "login"
        AuthState.Loading -> "splash" // Opcional: pantalla de carga
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen()
        }

        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            DynamicHomeScreen(
                onNavigateToDiets = {
                    navController.navigate("diets")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("diets") {
            DietListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onDietClick = { dietId ->
                    navController.navigate("diet_detail/$dietId")
                }
            )
        }

        composable("diet_detail/{dietId}") { backStackEntry ->
            val dietId = backStackEntry.arguments?.getString("dietId") ?: ""
            DietDetailScreen(
                dietId = dietId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMealClick = { mealId ->
                    navController.navigate("meal_recipe/$mealId")
                }
            )
        }

        composable("meal_recipe/{mealId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            MealRecipeScreen(
                mealId = mealId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("meal_selection") {
            MealSelectionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMealRecipeClick = { mealId ->
                    navController.navigate("meal_recipe/$mealId")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAlarms = {
                    navController.navigate("alarms")
                },
                onNavigateToMealSelection = {
                    navController.navigate("meal_selection")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onLogout = {
                    sessionViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("alarms") {
            AlarmConfigScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E90FF),
                        Color(0xFF60A5FA)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Logo
            Card(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🍓",
                        fontSize = 60.sp
                    )
                }
            }

            // Nombre de la app
            Text(
                text = "NutriAlarm",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Indicador de carga
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "Verificando sesión...",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}