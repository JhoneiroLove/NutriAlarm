package com.upao.nutrialarm.data.local.database

import com.upao.nutrialarm.data.local.database.entities.DietEntity
import com.upao.nutrialarm.data.local.database.entities.DietMealCrossRef
import com.upao.nutrialarm.data.local.database.entities.MealEntity
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.MealType

object PreloadedData {

    // Horarios predeterminados
    val DEFAULT_MEAL_TIMES = mapOf(
        MealType.BREAKFAST to "06:30",
        MealType.SCHOOL_SNACK to "09:30",
        MealType.LUNCH to "13:30",
        MealType.AFTERNOON_SNACK to "17:00",
        MealType.DINNER to "19:30",
        MealType.OPTIONAL_SNACK to "21:00"
    )

    // Comidas predeterminadas
    fun getPreloadedMeals(): List<MealEntity> {
        return listOf(
            // DESAYUNOS
            MealEntity(
                id = "breakfast_1",
                name = "Quinua con Leche y Frutas",
                description = "Quinua cocida con leche, plátano, manzana y miel",
                ingredients = """["1 taza quinua cocida", "1 taza leche", "1 plátano", "1 manzana", "1 cda miel", "Canela al gusto"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 4.2,
                calories = 380.0,
                preparationTime = 15,
                imageUrl = "",
                vitaminC = 45.0,
                folate = 85.0
            ),
            MealEntity(
                id = "breakfast_2",
                name = "Avena con Cacao y Frutos Secos",
                description = "Avena cocida con cacao, nueces, almendras y pasas",
                ingredients = """["1 taza avena", "2 cda cacao", "1 puñado nueces", "1 puñado almendras", "2 cda pasas", "1 taza leche"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 5.8,
                calories = 420.0,
                preparationTime = 10,
                imageUrl = "",
                vitaminC = 15.0,
                folate = 95.0
            ),
            MealEntity(
                id = "breakfast_3",
                name = "Pan Integral con Palta y Huevo",
                description = "Pan integral tostado con palta, huevo frito y tomate",
                ingredients = """["2 rebanadas pan integral", "1 palta", "1 huevo", "1 tomate", "Sal y pimienta"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 3.5,
                calories = 350.0,
                preparationTime = 8,
                imageUrl = "",
                vitaminC = 35.0,
                folate = 78.0
            ),

            // REFRIGERIOS ESCOLARES
            MealEntity(
                id = "school_snack_1",
                name = "Smoothie de Espinaca y Frutas",
                description = "Batido de espinaca, plátano, mango y yogur",
                ingredients = """["1 taza espinaca", "1 plátano", "1/2 mango", "1 yogur natural", "1 cda miel"]""",
                mealType = MealType.SCHOOL_SNACK.name,
                ironContent = 2.8,
                calories = 180.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 65.0,
                folate = 125.0
            ),
            MealEntity(
                id = "school_snack_2",
                name = "Granola con Yogur y Berries",
                description = "Granola casera con yogur y frutos rojos",
                ingredients = """["1/2 taza granola", "1 yogur griego", "1/4 taza arándanos", "1/4 taza fresas", "1 cda miel"]""",
                mealType = MealType.SCHOOL_SNACK.name,
                ironContent = 2.2,
                calories = 220.0,
                preparationTime = 3,
                imageUrl = "",
                vitaminC = 55.0,
                folate = 45.0
            ),

            // ALMUERZOS
            MealEntity(
                id = "lunch_1",
                name = "Lentejas Guisadas con Arroz",
                description = "Lentejas guisadas con verduras, arroz integral y ensalada",
                ingredients = """["1 taza lentejas", "1 taza arroz integral", "Zanahoria", "Apio", "Cebolla", "Tomate", "Lechuga", "Pepino"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 8.5,
                calories = 480.0,
                preparationTime = 45,
                imageUrl = "",
                vitaminC = 75.0,
                folate = 185.0
            ),
            MealEntity(
                id = "lunch_2",
                name = "Pollo a la Plancha con Quinua",
                description = "Pechuga de pollo a la plancha con quinua y verduras salteadas",
                ingredients = """["150g pechuga pollo", "1 taza quinua", "Brócoli", "Zanahoria", "Pimiento", "Aceite oliva"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 6.2,
                calories = 520.0,
                preparationTime = 30,
                imageUrl = "",
                vitaminC = 85.0,
                folate = 125.0
            ),
            MealEntity(
                id = "lunch_3",
                name = "Salmón al Horno con Espinacas",
                description = "Filete de salmón al horno con espinacas salteadas y camote",
                ingredients = """["150g salmón", "2 tazas espinaca", "1 camote mediano", "Ajo", "Limón", "Aceite oliva"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 7.8,
                calories = 450.0,
                preparationTime = 35,
                imageUrl = "",
                vitaminC = 95.0,
                folate = 145.0
            ),

            // MERIENDAS DE TARDE
            MealEntity(
                id = "afternoon_snack_1",
                name = "Hummus con Vegetales",
                description = "Hummus casero con bastones de zanahoria, apio y pepino",
                ingredients = """["1/2 taza hummus", "1 zanahoria", "2 tallos apio", "1 pepino", "Pimiento rojo"]""",
                mealType = MealType.AFTERNOON_SNACK.name,
                ironContent = 2.5,
                calories = 150.0,
                preparationTime = 10,
                imageUrl = "",
                vitaminC = 45.0,
                folate = 65.0
            ),
            MealEntity(
                id = "afternoon_snack_2",
                name = "Frutas Secas y Nueces",
                description = "Mix de frutas secas, nueces y semillas",
                ingredients = """["1/4 taza nueces", "1/4 taza almendras", "2 cda pasas", "2 cda arándanos secos", "1 cda semillas girasol"]""",
                mealType = MealType.AFTERNOON_SNACK.name,
                ironContent = 3.2,
                calories = 280.0,
                preparationTime = 2,
                imageUrl = "",
                vitaminC = 15.0,
                folate = 35.0
            ),

            // CENAS
            MealEntity(
                id = "dinner_1",
                name = "Sopa de Verduras con Legumbres",
                description = "Sopa nutritiva con garbanzos, verduras y pan integral",
                ingredients = """["1 taza garbanzos", "Zanahoria", "Apio", "Espinaca", "Cebolla", "Tomate", "2 rebanadas pan integral"]""",
                mealType = MealType.DINNER.name,
                ironContent = 5.5,
                calories = 320.0,
                preparationTime = 30,
                imageUrl = "",
                vitaminC = 65.0,
                folate = 155.0
            ),
            MealEntity(
                id = "dinner_2",
                name = "Tortilla de Espinacas",
                description = "Tortilla francesa con espinacas y queso, ensalada verde",
                ingredients = """["3 huevos", "2 tazas espinaca", "1/4 taza queso fresco", "Lechuga", "Tomate", "Pepino"]""",
                mealType = MealType.DINNER.name,
                ironContent = 4.8,
                calories = 280.0,
                preparationTime = 15,
                imageUrl = "",
                vitaminC = 55.0,
                folate = 185.0
            ),

            // SNACKS OPCIONALES
            MealEntity(
                id = "optional_snack_1",
                name = "Té de Hierbas con Galletas Integrales",
                description = "Infusión relajante con 2-3 galletas integrales",
                ingredients = """["1 taza té manzanilla", "3 galletas integrales", "1 cda miel"]""",
                mealType = MealType.OPTIONAL_SNACK.name,
                ironContent = 1.2,
                calories = 120.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 5.0,
                folate = 15.0
            ),
            MealEntity(
                id = "optional_snack_2",
                name = "Yogur con Semillas de Chía",
                description = "Yogur natural con semillas de chía y canela",
                ingredients = """["1 yogur natural", "1 cda semillas chía", "Canela al gusto", "1 cda miel"]""",
                mealType = MealType.OPTIONAL_SNACK.name,
                ironContent = 1.8,
                calories = 140.0,
                preparationTime = 3,
                imageUrl = "",
                vitaminC = 8.0,
                folate = 25.0
            )
        )
    }

    // Dietas predeterminadas
    fun getPreloadedDiets(): List<DietEntity> {
        return listOf(
            DietEntity(
                id = "diet_basic",
                name = "Dieta Básica Antianoemia",
                description = "Plan alimentario básico para prevenir la anemia con alimentos ricos en hierro",
                anemiaRiskLevel = AnemiaRisk.LOW.name,
                ironContent = 25.0,
                calories = 1800.0
            ),
            DietEntity(
                id = "diet_medium",
                name = "Dieta Reforzada con Hierro",
                description = "Plan alimentario reforzado para riesgo medio de anemia",
                anemiaRiskLevel = AnemiaRisk.MEDIUM.name,
                ironContent = 35.0,
                calories = 2000.0
            ),
            DietEntity(
                id = "diet_intensive",
                name = "Dieta Intensiva Antianoemia",
                description = "Plan alimentario intensivo para alto riesgo de anemia",
                anemiaRiskLevel = AnemiaRisk.HIGH.name,
                ironContent = 45.0,
                calories = 2200.0
            )
        )
    }

    // Relaciones dieta-comida
    fun getDietMealCrossRefs(): List<DietMealCrossRef> {
        return listOf(
            // Dieta Básica
            DietMealCrossRef("diet_basic", "breakfast_1"),
            DietMealCrossRef("diet_basic", "school_snack_1"),
            DietMealCrossRef("diet_basic", "lunch_1"),
            DietMealCrossRef("diet_basic", "afternoon_snack_1"),
            DietMealCrossRef("diet_basic", "dinner_1"),
            DietMealCrossRef("diet_basic", "optional_snack_1"),

            // Dieta Reforzada
            DietMealCrossRef("diet_medium", "breakfast_2"),
            DietMealCrossRef("diet_medium", "school_snack_2"),
            DietMealCrossRef("diet_medium", "lunch_2"),
            DietMealCrossRef("diet_medium", "afternoon_snack_2"),
            DietMealCrossRef("diet_medium", "dinner_2"),
            DietMealCrossRef("diet_medium", "optional_snack_2"),

            // Dieta Intensiva
            DietMealCrossRef("diet_intensive", "breakfast_3"),
            DietMealCrossRef("diet_intensive", "school_snack_1"),
            DietMealCrossRef("diet_intensive", "lunch_3"),
            DietMealCrossRef("diet_intensive", "afternoon_snack_1"),
            DietMealCrossRef("diet_intensive", "dinner_1"),
            DietMealCrossRef("diet_intensive", "optional_snack_1")
        )
    }
}