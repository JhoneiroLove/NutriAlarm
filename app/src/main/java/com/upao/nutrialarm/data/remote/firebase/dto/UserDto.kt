package com.upao.nutrialarm.data.remote.firebase.dto

data class UserDto(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val age: Int = 0,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val activityLevel: String = "",
    val anemiaRisk: String = "",
    val createdAt: Long = 0L
) {
    fun toDomain(): User {
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
}

fun User.toDto(): UserDto {
    return UserDto(
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

