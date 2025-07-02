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
        entityColumn = "id",
        associateBy = Junction(
            value = DietMealCrossRef::class,
            parentColumn = "dietId",
            entityColumn = "mealId"
        )
    )
    val meals: List<MealEntity>
)