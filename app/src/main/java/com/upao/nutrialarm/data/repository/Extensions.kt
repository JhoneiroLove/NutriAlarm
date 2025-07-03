package com.upao.nutrialarm.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upao.nutrialarm.data.local.database.entities.*
import com.upao.nutrialarm.domain.model.*

// User Extensions
fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        age = age,
        weight = weight,
        height = height,
        activityLevel = ActivityLevel.valueOf(activityLevel),
        anemiaRisk = AnemiaRisk.valueOf(anemiaRisk),
        createdAt = createdAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        age = age,
        weight = weight,
        height = height,
        activityLevel = activityLevel.name,
        anemiaRisk = anemiaRisk.name,
        createdAt = createdAt
    )
}

// Diet Extensions
fun DietEntity.toDomain(): Diet {
    return Diet(
        id = id,
        name = name,
        description = description,
        anemiaRiskLevel = AnemiaRisk.valueOf(anemiaRiskLevel),
        meals = emptyList(),
        ironContent = ironContent,
        calories = calories
    )
}

fun Diet.toEntity(): DietEntity {
    return DietEntity(
        id = id,
        name = name,
        description = description,
        anemiaRiskLevel = anemiaRiskLevel.name,
        ironContent = ironContent,
        calories = calories,
        isPreloaded = false
    )
}

// Meal Extensions
fun MealEntity.toDomain(): Meal {
    val gson = Gson()
    val ingredientsList: List<String> = try {
        gson.fromJson(ingredients, object : TypeToken<List<String>>() {}.type)
    } catch (e: Exception) {
        emptyList()
    }

    return Meal(
        id = id,
        name = name,
        description = description,
        ingredients = ingredientsList,
        mealType = MealType.valueOf(mealType),
        ironContent = ironContent,
        calories = calories,
        preparationTime = preparationTime,
        imageUrl = imageUrl,
        vitaminC = vitaminC,
        folate = folate
    )
}

fun Meal.toEntity(): MealEntity {
    val gson = Gson()
    return MealEntity(
        id = id,
        name = name,
        description = description,
        ingredients = gson.toJson(ingredients),
        mealType = mealType.name,
        ironContent = ironContent,
        calories = calories,
        preparationTime = preparationTime,
        imageUrl = imageUrl,
        vitaminC = vitaminC,
        folate = folate,
        isPreloaded = false
    )
}

// Alarm Extensions
fun AlarmEntity.toDomain(): Alarm {
    val gson = Gson()
    val daysList: List<DayOfWeek> = try {
        gson.fromJson(days, object : TypeToken<List<DayOfWeek>>() {}.type)
    } catch (e: Exception) {
        emptyList()
    }

    return Alarm(
        id = id,
        userId = userId,
        mealType = MealType.valueOf(mealType),
        time = time,
        isEnabled = isEnabled,
        days = daysList,
        reminderMessage = reminderMessage,
        createdAt = createdAt
    )
}

fun Alarm.toEntity(): AlarmEntity {
    val gson = Gson()
    return AlarmEntity(
        id = id,
        userId = userId,
        mealType = mealType.name,
        time = time,
        isEnabled = isEnabled,
        days = gson.toJson(days),
        reminderMessage = reminderMessage,
        createdAt = createdAt
    )
}

// UserMealPreference Extensions
fun UserMealPreferenceEntity.toDomain(): UserMealPreference {
    return UserMealPreference(
        id = id,
        userId = userId,
        mealType = MealType.valueOf(mealType),
        selectedMealId = selectedMealId,
        timeSlot = timeSlot,
        isActive = isActive,
        reminderEnabled = reminderEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserMealPreference.toEntity(): UserMealPreferenceEntity {
    return UserMealPreferenceEntity(
        id = id,
        userId = userId,
        mealType = mealType.name,
        selectedMealId = selectedMealId,
        timeSlot = timeSlot,
        isActive = isActive,
        reminderEnabled = reminderEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

// MealConsumption Extensions
fun MealConsumptionEntity.toDomain(): MealConsumption {
    return MealConsumption(
        id = id,
        userId = userId,
        mealId = mealId,
        mealType = MealType.valueOf(mealType),
        consumedAt = consumedAt,
        date = date,
        ironContent = ironContent,
        calories = calories,
        vitaminC = vitaminC,
        folate = folate
    )
}

fun MealConsumption.toEntity(): MealConsumptionEntity {
    return MealConsumptionEntity(
        id = id,
        userId = userId,
        mealId = mealId,
        mealType = mealType.name,
        consumedAt = consumedAt,
        date = date,
        ironContent = ironContent,
        calories = calories,
        vitaminC = vitaminC,
        folate = folate
    )
}