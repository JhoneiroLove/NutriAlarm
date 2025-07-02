package com.upao.nutrialarm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upao.nutrialarm.presentation.auth.LoginScreen
import com.upao.nutrialarm.presentation.diet.DietListScreen
import com.upao.nutrialarm.presentation.home.HomeScreen
import com.upao.nutrialarm.presentation.profile.ProfileScreen

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
                onLoginClick = { email, password ->
                    // TODO: Implementar lógica de login
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
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
                }
            )
        }
    }
}