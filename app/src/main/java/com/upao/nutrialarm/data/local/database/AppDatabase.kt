package com.upao.nutrialarm.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.upao.nutrialarm.data.local.database.dao.*
import com.upao.nutrialarm.data.local.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        DietEntity::class,
        MealEntity::class,
        DietMealCrossRef::class,
        AlarmEntity::class,
        UserMealPreferenceEntity::class,
        MealConsumptionEntity::class
    ],
    version = 3, // INCREMENTAR VERSIÓN
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun dietDao(): DietDao
    abstract fun mealDao(): MealDao
    abstract fun alarmDao(): AlarmDao
    abstract fun userMealPreferenceDao(): UserMealPreferenceDao
    abstract fun mealConsumptionDao(): MealConsumptionDao // NUEVO DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutrialarm_database"
                )
                    .fallbackToDestructiveMigration() // Para desarrollo - quitar en producción
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}