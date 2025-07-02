package com.upao.nutrialarm.data.local.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.upao.nutrialarm.data.local.database.entities.DietEntity
import com.upao.nutrialarm.data.local.database.entities.DietMealCrossRef
import com.upao.nutrialarm.data.local.database.entities.MealEntity

data class DietWithMeals(
    @Embedded val diet: DietEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId",
        associateBy = Junction(DietMealCrossRef::class)
    )
    val meals: List<MealEntity>
)