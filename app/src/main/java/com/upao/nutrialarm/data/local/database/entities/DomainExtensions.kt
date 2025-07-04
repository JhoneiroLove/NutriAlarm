package com.upao.nutrialarm.data.local.database.entities

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upao.nutrialarm.domain.model.*

fun MealEntity.toDomain(): Meal {
    val ingredientsList = try {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        gson.fromJson<List<String>>(this.ingredients, type) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    return Meal(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredients = ingredientsList,
        mealType = MealType.valueOf(this.mealType),
        ironContent = this.ironContent,
        calories = this.calories,
        preparationTime = this.preparationTime,
        imageUrl = this.imageUrl,
        vitaminC = this.vitaminC,
        folate = this.folate
    )
}

fun Meal.toEntity(): MealEntity {
    val gson = Gson()
    val ingredientsJson = gson.toJson(this.ingredients)

    return MealEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        ingredients = ingredientsJson,
        mealType = this.mealType.name,
        ironContent = this.ironContent,
        calories = this.calories,
        preparationTime = this.preparationTime,
        imageUrl = this.imageUrl,
        vitaminC = this.vitaminC,
        folate = this.folate,
        isPreloaded = false
    )
}

fun DietEntity.toDomain(): Diet {
    return Diet(
        id = this.id,
        name = this.name,
        description = this.description,
        anemiaRiskLevel = AnemiaRisk.valueOf(this.anemiaRiskLevel),
        meals = emptyList(), // Se llenará por separado en el repository
        ironContent = this.ironContent,
        calories = this.calories
    )
}

fun Diet.toEntity(): DietEntity {
    return DietEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        anemiaRiskLevel = this.anemiaRiskLevel.name,
        ironContent = this.ironContent,
        calories = this.calories,
        isPreloaded = false
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        name = this.name,
        age = this.age,
        weight = this.weight,
        height = this.height,
        activityLevel = ActivityLevel.valueOf(this.activityLevel),
        anemiaRisk = AnemiaRisk.valueOf(this.anemiaRisk),
        createdAt = this.createdAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        name = this.name,
        age = this.age,
        weight = this.weight,
        height = this.height,
        activityLevel = this.activityLevel.name,
        anemiaRisk = this.anemiaRisk.name,
        createdAt = this.createdAt
    )
}

fun MealConsumptionEntity.toDomain(): MealConsumption {
    return MealConsumption(
        id = this.id,
        userId = this.userId,
        mealId = this.mealId,
        mealType = MealType.valueOf(this.mealType),
        consumedAt = this.consumedAt,
        date = this.date,
        ironContent = this.ironContent,
        calories = this.calories,
        vitaminC = this.vitaminC,
        folate = this.folate
    )
}

fun MealConsumption.toEntity(): MealConsumptionEntity {
    return MealConsumptionEntity(
        id = this.id,
        userId = this.userId,
        mealId = this.mealId,
        mealType = this.mealType.name,
        consumedAt = this.consumedAt,
        date = this.date,
        ironContent = this.ironContent,
        calories = this.calories,
        vitaminC = this.vitaminC,
        folate = this.folate
    )
}

fun UserMealPreferenceEntity.toDomain(): UserMealPreference {
    return UserMealPreference(
        id = this.id,
        userId = this.userId,
        mealType = MealType.valueOf(this.mealType),
        selectedMealId = this.selectedMealId,
        timeSlot = this.timeSlot,
        isActive = this.isActive,
        reminderEnabled = this.reminderEnabled,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun UserMealPreference.toEntity(): UserMealPreferenceEntity {
    return UserMealPreferenceEntity(
        id = this.id,
        userId = this.userId,
        mealType = this.mealType.name,
        selectedMealId = this.selectedMealId,
        timeSlot = this.timeSlot,
        isActive = this.isActive,
        reminderEnabled = this.reminderEnabled,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun AlarmEntity.toDomain(): Alarm {
    val daysList = try {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        val daysStrings = gson.fromJson<List<String>>(this.days, type) ?: emptyList()
        daysStrings.map { DayOfWeek.valueOf(it) }
    } catch (e: Exception) {
        emptyList()
    }

    return Alarm(
        id = this.id,
        userId = this.userId,
        mealType = MealType.valueOf(this.mealType),
        time = this.time,
        isEnabled = this.isEnabled,
        days = daysList,
        reminderMessage = this.reminderMessage,
        createdAt = this.createdAt
    )
}

fun Alarm.toEntity(): AlarmEntity {
    val gson = Gson()
    val daysJson = gson.toJson(this.days.map { it.name })

    return AlarmEntity(
        id = this.id,
        userId = this.userId,
        mealType = this.mealType.name,
        time = this.time,
        isEnabled = this.isEnabled,
        days = daysJson,
        reminderMessage = this.reminderMessage,
        createdAt = this.createdAt
    )
}