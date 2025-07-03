package com.upao.nutrialarm.presentation.navigation

import androidx.navigation.NavHostController

// Extensiones útiles para la navegación
fun NavHostController.navigateToMealRecipe(mealId: String) {
    navigate("meal_recipe/$mealId")
}

fun NavHostController.navigateToDietDetail(dietId: String) {
    navigate("diet_detail/$dietId")
}

fun NavHostController.navigateToProfileSection(section: ProfileSection) {
    when (section) {
        ProfileSection.ALARMS -> navigate("alarms")
        ProfileSection.MEAL_SELECTION -> navigate("meal_selection")
        ProfileSection.SETTINGS -> navigate("settings")
        ProfileSection.EDIT_PROFILE -> navigate("edit_profile")
    }
}

fun NavHostController.navigateToHomeAndClearStack() {
    navigate("home") {
        popUpTo(0) { inclusive = true }
    }
}

fun NavHostController.navigateToLoginAndClearStack() {
    navigate("login") {
        popUpTo(0) { inclusive = true }
    }
}

enum class ProfileSection {
    ALARMS, MEAL_SELECTION, SETTINGS, EDIT_PROFILE
}