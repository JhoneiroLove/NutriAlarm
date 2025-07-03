package com.upao.nutrialarm.presentation.navigation

import androidx.compose.runtime.Composable
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
import com.upao.nutrialarm.presentation.home.DynamicHomeScreen // CAMBIADO
import com.upao.nutrialarm.presentation.profile.ProfileScreen
import com.upao.nutrialarm.presentation.alarm.AlarmConfigScreen
import com.upao.nutrialarm.presentation.settings.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // ========================================
        // AUTENTICACIÓN
        // ========================================
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

        // ========================================
        // PANTALLA PRINCIPAL - AHORA DINÁMICA
        // ========================================
        composable("home") {
            DynamicHomeScreen( // USAR LA NUEVA PANTALLA DINÁMICA
                onNavigateToDiets = {
                    navController.navigate("diets")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        // ========================================
        // DIETAS Y COMIDAS
        // ========================================
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

        // ========================================
        // SELECCIÓN PERSONALIZADA DE MENÚS
        // ========================================
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

        // ========================================
        // PERFIL Y CONFIGURACIÓN
        // ========================================
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
                }
            )
        }

        // ========================================
        // ALARMAS
        // ========================================
        composable("alarms") {
            AlarmConfigScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ========================================
        // CONFIGURACIÓN
        // ========================================
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}