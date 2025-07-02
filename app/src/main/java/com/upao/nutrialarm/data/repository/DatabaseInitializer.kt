package com.upao.nutrialarm.data.repository

import android.util.Log
import com.upao.nutrialarm.data.local.database.AppDatabase
import com.upao.nutrialarm.data.local.database.PreloadedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val database: AppDatabase
) {

    suspend fun initializePreloadedData() {
        withContext(Dispatchers.IO) {
            try {
                // Limpiar datos previos
                database.dietDao().deletePreloadedDiets()
                database.mealDao().deletePreloadedMeals()
                database.mealDao().deleteAllCrossRefs()

                // Insertar nuevos datos
                val meals = PreloadedData.getPreloadedMeals()
                val diets = PreloadedData.getPreloadedDiets()
                val crossRefs = PreloadedData.getDietMealCrossRefs()

                database.mealDao().insertMeals(meals)
                database.dietDao().insertDiets(diets)
                database.mealDao().insertDietMealCrossRefs(crossRefs)

                Log.d("DatabaseInitializer", "Datos predeterminados cargados exitosamente")

            } catch (e: Exception) {
                Log.e("DatabaseInitializer", "Error al cargar datos predeterminados", e)
            }
        }
    }
}