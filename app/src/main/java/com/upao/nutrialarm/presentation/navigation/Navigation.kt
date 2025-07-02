package com.upao.nutrialarm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upao.nutrialarm.presentation.auth.LoginScreen
import com.upao.nutrialarm.presentation.auth.RegisterScreen
import com.upao.nutrialarm.presentation.diet.DietListScreen
import com.upao.nutrialarm.presentation.home.HomeScreen
import com.upao.nutrialarm.presentation.profile.ProfileScreen
import com.upao.nutrialarm.presentation.alarm.AlarmConfigScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
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
            HomeScreen(
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
    }
}