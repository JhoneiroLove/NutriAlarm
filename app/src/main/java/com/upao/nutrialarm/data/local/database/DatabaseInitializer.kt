package com.upao.nutrialarm.data.local.database

import android.content.Context
import android.util.Log
import com.upao.nutrialarm.data.local.database.dao.*
import com.upao.nutrialarm.domain.model.MealType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mealDao: MealDao,
    private val dietDao: DietDao,
    private val userDao: UserDao
) {

    companion object {
        private const val TAG = "DatabaseInitializer"
        private const val PREFS_NAME = "nutrialarm_prefs"
        private const val KEY_DB_INITIALIZED = "db_initialized"
        private const val DB_VERSION = 1
    }

    suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        try {
            if (isDatabaseInitialized()) {
                Log.d(TAG, "Database already initialized")
                return@withContext
            }

            Log.d(TAG, "Starting database initialization...")

            // Limpiar datos existentes si es necesario
            clearExistingData()

            // Insertar datos predefinidos
            insertPreloadedMeals()
            insertPreloadedDiets()
            insertDietMealRelations()

            // Marcar como inicializada
            markDatabaseAsInitialized()

            Log.d(TAG, "Database initialization completed successfully")

        } catch (e: Exception) {
            Log.e(TAG, "Error initializing database", e)
            throw e
        }
    }

    private suspend fun clearExistingData() {
        try {
            mealDao.deleteAllCrossRefs()
            mealDao.deletePreloadedMeals()
            dietDao.deletePreloadedDiets()
            Log.d(TAG, "Existing preloaded data cleared")
        } catch (e: Exception) {
            Log.w(TAG, "Error clearing existing data: ${e.message}")
        }
    }

    private suspend fun insertPreloadedMeals() {
        val meals = PreloadedData.getPreloadedMeals()
        mealDao.insertMeals(meals)
        Log.d(TAG, "Inserted ${meals.size} preloaded meals")
    }

    private suspend fun insertPreloadedDiets() {
        val diets = PreloadedData.getPreloadedDiets()
        dietDao.insertDiets(diets)
        Log.d(TAG, "Inserted ${diets.size} preloaded diets")
    }

    private suspend fun insertDietMealRelations() {
        val crossRefs = PreloadedData.getDietMealCrossRefs()
        mealDao.insertDietMealCrossRefs(crossRefs)
        Log.d(TAG, "Inserted ${crossRefs.size} diet-meal relations")
    }

    private fun isDatabaseInitialized(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedVersion = prefs.getInt(KEY_DB_INITIALIZED, 0)
        return savedVersion >= DB_VERSION
    }

    private fun markDatabaseAsInitialized() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_DB_INITIALIZED, DB_VERSION).apply()
    }

    // Método para verificar la integridad de los datos
    suspend fun verifyDataIntegrity(): DataIntegrityResult = withContext(Dispatchers.IO) {
        try {
            val mealCounts = mutableMapOf<MealType, Int>()
            var totalMeals = 0
            var totalDiets = 0
            var totalRelations = 0

            // Contar comidas por tipo
            MealType.values().forEach { mealType ->
                val count = mealDao.getMealsByType(mealType.name).size
                mealCounts[mealType] = count
                totalMeals += count
            }

            // Contar dietas
            totalDiets = dietDao.getAllDiets().size

            // Contar relaciones (aproximado)
            totalRelations = PreloadedData.getDietMealCrossRefs().size

            DataIntegrityResult(
                isValid = totalMeals > 0 && totalDiets > 0,
                totalMeals = totalMeals,
                totalDiets = totalDiets,
                totalRelations = totalRelations,
                mealCountsByType = mealCounts,
                errorMessage = null
            )

        } catch (e: Exception) {
            Log.e(TAG, "Error verifying data integrity", e)
            DataIntegrityResult(
                isValid = false,
                totalMeals = 0,
                totalDiets = 0,
                totalRelations = 0,
                mealCountsByType = emptyMap(),
                errorMessage = e.message
            )
        }
    }

    // Método para forzar reinicialización (útil para desarrollo)
    suspend fun forceReinitialize() = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Forcing database reinitialization...")

            // Limpiar flag de inicialización
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove(KEY_DB_INITIALIZED).apply()

            // Reinicializar
            initializeDatabase()

            Log.d(TAG, "Database reinitialization completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error during forced reinitialization", e)
            throw e
        }
    }
}

data class DataIntegrityResult(
    val isValid: Boolean,
    val totalMeals: Int,
    val totalDiets: Int,
    val totalRelations: Int,
    val mealCountsByType: Map<MealType, Int>,
    val errorMessage: String?
)