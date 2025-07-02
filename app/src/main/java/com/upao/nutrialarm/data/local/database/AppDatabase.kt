package com.upao.nutrialarm.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.upao.nutrialarm.data.local.database.dao.DietDao
import com.upao.nutrialarm.data.local.database.dao.MealDao
import com.upao.nutrialarm.data.local.database.dao.UserDao
import com.upao.nutrialarm.data.local.database.dao.AlarmDao
import com.upao.nutrialarm.data.local.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        DietEntity::class,
        MealEntity::class,
        DietMealCrossRef::class,
        AlarmEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun dietDao(): DietDao
    abstract fun mealDao(): MealDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutrialarm_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}