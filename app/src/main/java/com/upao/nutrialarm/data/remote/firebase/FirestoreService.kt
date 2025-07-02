package com.upao.nutrialarm.data.remote.firebase

@Singleton
class FirestoreService @Inject constructor() {

    private val firestore = Firebase.firestore

    // Colecciones
    companion object {
        const val USERS_COLLECTION = "users"
        const val DIETS_COLLECTION = "diets"
        const val USER_DIETS_COLLECTION = "user_diets"
        const val MEALS_COLLECTION = "meals"
    }

    // Usuarios
    suspend fun saveUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(USERS_COLLECTION)
                .document(user.id)
                .set(user.toDto())
                .await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<User?> = withContext(Dispatchers.IO) {
        try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            val user = document.toObject<UserDto>()?.toDomain()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Dietas
    suspend fun getDiets(): Result<List<Diet>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection(DIETS_COLLECTION)
                .get()
                .await()

            val diets = snapshot.documents.mapNotNull {
                it.toObject<DietDto>()?.toDomain()
            }
            Result.success(diets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDietsByRiskLevel(riskLevel: AnemiaRisk): Result<List<Diet>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection(DIETS_COLLECTION)
                .whereEqualTo("anemiaRiskLevel", riskLevel.name)
                .get()
                .await()

            val diets = snapshot.documents.mapNotNull {
                it.toObject<DietDto>()?.toDomain()
            }
            Result.success(diets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Comidas por tipo
    suspend fun getMealsByType(mealType: MealType): Result<List<Meal>> = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection(MEALS_COLLECTION)
                .whereEqualTo("mealType", mealType.name)
                .get()
                .await()

            val meals = snapshot.documents.mapNotNull {
                it.toObject<MealDto>()?.toDomain()
            }
            Result.success(meals)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}