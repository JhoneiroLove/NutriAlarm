package com.upao.nutrialarm.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diets")
data class DietEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val anemiaRiskLevel: String,
    val ironContent: Double,
    val calories: Double,
    val isPreloaded: Boolean = true // Para dietas predeterminadas
)
