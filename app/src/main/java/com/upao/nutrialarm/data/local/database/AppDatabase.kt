package com.upao.nutrialarm.data.local.database

import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.upao.nutrialarm.data.local.database.dao.DietDao
import com.upao.nutrialarm.data.local.database.dao.MealDao

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
}
