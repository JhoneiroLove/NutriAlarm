package com.upao.nutrialarm.presentation.navigation

object NavigationConstants {
    // Rutas principales
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    // Rutas de contenido
    const val DIETS = "diets"
    const val DIET_DETAIL = "diet_detail/{dietId}"
    const val MEAL_RECIPE = "meal_recipe/{mealId}"
    const val MEAL_SELECTION = "meal_selection"

    // Rutas de perfil
    const val PROFILE = "profile"
    const val ALARMS = "alarms"
    const val SETTINGS = "settings"
    const val EDIT_PROFILE = "edit_profile"

    // Rutas futuras
    const val NUTRITION_STATS = "nutrition_stats"
    const val MEAL_HISTORY = "meal_history"
    const val ONBOARDING = "onboarding"

    // Argumentos
    const val ARG_DIET_ID = "dietId"
    const val ARG_MEAL_ID = "mealId"
    const val ARG_USER_ID = "userId"

    // Funciones helper para crear rutas con parámetros
    fun dietDetailRoute(dietId: String) = "diet_detail/$dietId"
    fun mealRecipeRoute(mealId: String) = "meal_recipe/$mealId"
}