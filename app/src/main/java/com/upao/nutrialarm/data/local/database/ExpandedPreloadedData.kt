package com.upao.nutrialarm.data.local.database

import com.upao.nutrialarm.data.local.database.entities.DietEntity
import com.upao.nutrialarm.data.local.database.entities.DietMealCrossRef
import com.upao.nutrialarm.data.local.database.entities.MealEntity
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.MealType

object ExpandedPreloadedData {

    fun getExpandedMeals(): List<MealEntity> {
        return listOf(
            // ================ DESAYUNOS (15 opciones) ================
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
            MealEntity(
                id = "breakfast_4",
                name = "Smoothie de Espinaca y Mango",
                description = "Batido verde con espinaca, mango, plátano y yogur",
                ingredients = """["2 tazas espinaca fresca", "1 mango", "1 plátano", "1 yogur natural", "1 cda miel", "Agua"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 3.8,
                calories = 280.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 85.0,
                folate = 145.0
            ),
            MealEntity(
                id = "breakfast_5",
                name = "Tostadas Francesas Integrales",
                description = "Pan integral en tostadas francesas con canela y frutas",
                ingredients = """["3 rebanadas pan integral", "2 huevos", "1/2 taza leche", "Canela", "1 taza fresas", "Miel"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 4.1,
                calories = 395.0,
                preparationTime = 12,
                imageUrl = "",
                vitaminC = 65.0,
                folate = 88.0
            ),
            MealEntity(
                id = "breakfast_6",
                name = "Bowl de Yogur con Granola",
                description = "Yogur griego con granola casera, frutos rojos y semillas",
                ingredients = """["1 taza yogur griego", "1/2 taza granola", "1/2 taza arándanos", "1/4 taza fresas", "1 cda semillas chía", "Miel"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 3.2,
                calories = 365.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 55.0,
                folate = 45.0
            ),
            MealEntity(
                id = "breakfast_7",
                name = "Huevos Revueltos con Vegetales",
                description = "Huevos revueltos con espinaca, tomate y queso fresco",
                ingredients = """["3 huevos", "1 taza espinaca", "1 tomate", "1/4 taza queso fresco", "Aceite oliva", "Hierbas"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 4.8,
                calories = 320.0,
                preparationTime = 10,
                imageUrl = "",
                vitaminC = 42.0,
                folate = 125.0
            ),
            MealEntity(
                id = "breakfast_8",
                name = "Pancakes de Plátano y Avena",
                description = "Pancakes saludables hechos con plátano, avena y huevo",
                ingredients = """["2 plátanos maduros", "1/2 taza avena", "2 huevos", "1/2 cda canela", "1 cda aceite coco", "Frutas para decorar"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 3.6,
                calories = 340.0,
                preparationTime = 15,
                imageUrl = "",
                vitaminC = 25.0,
                folate = 65.0
            ),
            MealEntity(
                id = "breakfast_9",
                name = "Muesli con Frutas Secas",
                description = "Muesli casero con frutas secas, nueces y leche",
                ingredients = """["1/2 taza muesli", "1 taza leche", "2 cda pasas", "1/4 taza nueces", "1/2 manzana", "Canela"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 4.5,
                calories = 410.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 18.0,
                folate = 75.0
            ),
            MealEntity(
                id = "breakfast_10",
                name = "Tortilla de Claras con Vegetales",
                description = "Tortilla ligera de claras con espárragos y champiñones",
                ingredients = """["4 claras de huevo", "1 yema", "6 espárragos", "1/2 taza champiñones", "Tomate cherry", "Hierbas frescas"]""",
                mealType = MealType.BREAKFAST.name,
                ironContent = 3.8,
                calories = 285.0,
                preparationTime = 12,
                imageUrl = "",
                vitaminC = 38.0,
                folate = 115.0
            ),

            // ================ REFRIGERIOS ESCOLARES (12 opciones) ================
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
            MealEntity(
                id = "school_snack_3",
                name = "Sandwich de Mantequilla de Maní",
                description = "Pan integral con mantequilla de maní y plátano",
                ingredients = """["2 rebanadas pan integral", "2 cda mantequilla maní", "1 plátano", "1 cda miel", "Canela"]""",
                mealType = MealType.SCHOOL_SNACK.name,
                ironContent = 2.5,
                calories = 285.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 12.0,
                folate = 35.0
            ),
            MealEntity(
                id = "school_snack_4",
                name = "Frutas con Yogur y Granola",
                description = "Mix de frutas frescas con yogur y granola crujiente",
                ingredients = """["1/2 taza fresas", "1/2 taza uvas", "1 kiwi", "1 yogur natural", "2 cda granola"]""",
                mealType = MealType.SCHOOL_SNACK.name,
                ironContent = 1.8,
                calories = 195.0,
                preparationTime = 3,
                imageUrl = "",
                vitaminC = 85.0,
                folate = 28.0
            ),
            MealEntity(
                id = "school_snack_5",
                name = "Barras de Avena Caseras",
                description = "Barras energéticas de avena con pasas y nueces",
                ingredients = """["1 taza avena", "1/4 taza nueces", "2 cda pasas", "2 cda miel", "1 cda aceite coco"]""",
                mealType = MealType.SCHOOL_SNACK.name,
                ironContent = 2.4,
                calories = 245.0,
                preparationTime = 20,
                imageUrl = "",
                vitaminC = 8.0,
                folate = 32.0
            ),
            MealEntity(
                id = "school_snack_6",
                name = "Wrap de Hummus y Vegetales",
                description = "Tortilla integral con hummus, zanahoria y pepino",
                ingredients = """["1 tortilla integral", "3 cda hummus", "1/2 zanahoria", "1/4 pepino", "Hojas de lechuga"]""",
                mealType = MealType.SCHOOL_SNACK.name,
                ironContent = 2.1,
                calories = 165.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 22.0,
                folate = 45.0
            ),

            // ================ ALMUERZOS (18 opciones) ================
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
            MealEntity(
                id = "lunch_4",
                name = "Estofado de Res con Vegetales",
                description = "Carne de res estofada con papa, zanahoria y arvejas",
                ingredients = """["150g carne res", "2 papas", "1 zanahoria", "1/2 taza arvejas", "Cebolla", "Tomate", "Especias"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 9.2,
                calories = 485.0,
                preparationTime = 60,
                imageUrl = "",
                vitaminC = 45.0,
                folate = 95.0
            ),
            MealEntity(
                id = "lunch_5",
                name = "Pasta Integral con Salsa de Espinacas",
                description = "Pasta integral con salsa cremosa de espinacas y queso",
                ingredients = """["150g pasta integral", "2 tazas espinaca", "1/4 taza queso parmesano", "2 dientes ajo", "Aceite oliva", "Crema light"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 5.8,
                calories = 425.0,
                preparationTime = 25,
                imageUrl = "",
                vitaminC = 35.0,
                folate = 165.0
            ),
            MealEntity(
                id = "lunch_6",
                name = "Tacos de Frijoles Negros",
                description = "Tortillas de maíz con frijoles negros, aguacate y vegetales",
                ingredients = """["3 tortillas maíz", "1 taza frijoles negros", "1 aguacate", "Tomate", "Cebolla", "Cilantro", "Limón"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 7.5,
                calories = 395.0,
                preparationTime = 15,
                imageUrl = "",
                vitaminC = 42.0,
                folate = 145.0
            ),
            MealEntity(
                id = "lunch_7",
                name = "Arroz Chaufa de Pollo",
                description = "Arroz frito peruano con pollo, huevo y vegetales",
                ingredients = """["1.5 taza arroz cocido", "100g pollo", "1 huevo", "Cebolla china", "Pimiento", "Sillao", "Aceite"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 4.8,
                calories = 465.0,
                preparationTime = 20,
                imageUrl = "",
                vitaminC = 38.0,
                folate = 85.0
            ),
            MealEntity(
                id = "lunch_8",
                name = "Sopa Criolla con Pan",
                description = "Sopa sustanciosa con fideos, leche y huevo, servida con pan",
                ingredients = """["150g fideos cabello ángel", "2 huevos", "1 taza leche", "Queso fresco", "Pan francés", "Perejil"]""",
                mealType = MealType.LUNCH.name,
                ironContent = 5.2,
                calories = 445.0,
                preparationTime = 25,
                imageUrl = "",
                vitaminC = 15.0,
                folate = 95.0
            ),

            // ================ MERIENDAS DE TARDE (10 opciones) ================
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
            MealEntity(
                id = "afternoon_snack_3",
                name = "Batido de Proteínas Verde",
                description = "Smoothie con espinaca, piña, apio y proteína vegetal",
                ingredients = """["1 taza espinaca", "1/2 taza piña", "1 tallo apio", "1 scoop proteína vegetal", "Agua de coco"]""",
                mealType = MealType.AFTERNOON_SNACK.name,
                ironContent = 4.1,
                calories = 185.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 78.0,
                folate = 95.0
            ),
            MealEntity(
                id = "afternoon_snack_4",
                name = "Tostadas de Aguacate",
                description = "Pan integral tostado con aguacate, tomate cherry y semillas",
                ingredients = """["1 rebanada pan integral", "1/2 aguacate", "4 tomates cherry", "1 cda semillas calabaza", "Limón", "Sal"]""",
                mealType = MealType.AFTERNOON_SNACK.name,
                ironContent = 2.8,
                calories = 195.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 28.0,
                folate = 55.0
            ),

            // ================ CENAS (15 opciones) ================
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
            MealEntity(
                id = "dinner_3",
                name = "Pescado al Vapor con Vegetales",
                description = "Filete de pescado al vapor con brócoli y zanahoria",
                ingredients = """["150g filete pescado", "1 taza brócoli", "1 zanahoria", "Limón", "Hierbas finas", "Aceite oliva"]""",
                mealType = MealType.DINNER.name,
                ironContent = 3.8,
                calories = 245.0,
                preparationTime = 20,
                imageUrl = "",
                vitaminC = 92.0,
                folate = 85.0
            ),
            MealEntity(
                id = "dinner_4",
                name = "Ensalada César con Pollo",
                description = "Ensalada fresca con pollo a la plancha y aderezo ligero",
                ingredients = """["100g pechuga pollo", "Lechuga romana", "Tomates cherry", "Queso parmesano", "Crutones integrales", "Aderezo césar light"]""",
                mealType = MealType.DINNER.name,
                ironContent = 3.2,
                calories = 285.0,
                preparationTime = 15,
                imageUrl = "",
                vitaminC = 35.0,
                folate = 65.0
            ),
            MealEntity(
                id = "dinner_5",
                name = "Crema de Espárragos",
                description = "Sopa cremosa de espárragos con tostadas integrales",
                ingredients = """["500g espárragos", "1 papa", "Cebolla", "Caldo vegetal", "Crema light", "2 tostadas integrales"]""",
                mealType = MealType.DINNER.name,
                ironContent = 4.2,
                calories = 195.0,
                preparationTime = 25,
                imageUrl = "",
                vitaminC = 45.0,
                folate = 125.0
            ),

            // ================ SNACKS OPCIONALES (8 opciones) ================
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
            ),
            MealEntity(
                id = "optional_snack_3",
                name = "Manzana con Mantequilla de Almendras",
                description = "Rodajas de manzana con mantequilla natural de almendras",
                ingredients = """["1 manzana", "2 cda mantequilla almendras", "Canela en polvo"]""",
                mealType = MealType.OPTIONAL_SNACK.name,
                ironContent = 1.5,
                calories = 185.0,
                preparationTime = 3,
                imageUrl = "",
                vitaminC = 8.4,
                folate = 12.0
            ),
            MealEntity(
                id = "optional_snack_4",
                name = "Infusión de Jengibre con Miel",
                description = "Té caliente de jengibre con miel y limón",
                ingredients = """["1 trozo jengibre fresco", "1 cda miel", "1/2 limón", "1 taza agua caliente"]""",
                mealType = MealType.OPTIONAL_SNACK.name,
                ironContent = 0.5,
                calories = 45.0,
                preparationTime = 5,
                imageUrl = "",
                vitaminC = 25.0,
                folate = 3.0
            )
        )
    }

    // Dietas expandidas con más variedad
    fun getExpandedDiets(): List<DietEntity> {
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
            ),
            DietEntity(
                id = "diet_vegetarian",
                name = "Dieta Vegetariana Rica en Hierro",
                description = "Plan vegetariano optimizado para absorción de hierro",
                anemiaRiskLevel = AnemiaRisk.MEDIUM.name,
                ironContent = 38.0,
                calories = 1950.0
            ),
            DietEntity(
                id = "diet_student",
                name = "Dieta para Estudiantes",
                description = "Plan alimentario adaptado a horarios escolares",
                anemiaRiskLevel = AnemiaRisk.LOW.name,
                ironContent = 28.0,
                calories = 1850.0
            ),
            DietEntity(
                id = "diet_active",
                name = "Dieta para Vida Activa",
                description = "Plan para personas con alta actividad física",
                anemiaRiskLevel = AnemiaRisk.MEDIUM.name,
                ironContent = 42.0,
                calories = 2300.0
            )
        )
    }

    // Relaciones expandidas dieta-comida
    fun getExpandedDietMealCrossRefs(): List<DietMealCrossRef> {
        return listOf(
            // Dieta Básica
            DietMealCrossRef("diet_basic", "breakfast_1"),
            DietMealCrossRef("diet_basic", "breakfast_6"),
            DietMealCrossRef("diet_basic", "school_snack_1"),
            DietMealCrossRef("diet_basic", "school_snack_2"),
            DietMealCrossRef("diet_basic", "lunch_1"),
            DietMealCrossRef("diet_basic", "lunch_2"),
            DietMealCrossRef("diet_basic", "afternoon_snack_1"),
            DietMealCrossRef("diet_basic", "afternoon_snack_2"),
            DietMealCrossRef("diet_basic", "dinner_1"),
            DietMealCrossRef("diet_basic", "dinner_2"),
            DietMealCrossRef("diet_basic", "optional_snack_1"),

            // Dieta Reforzada
            DietMealCrossRef("diet_medium", "breakfast_2"),
            DietMealCrossRef("diet_medium", "breakfast_4"),
            DietMealCrossRef("diet_medium", "breakfast_7"),
            DietMealCrossRef("diet_medium", "school_snack_3"),
            DietMealCrossRef("diet_medium", "school_snack_5"),
            DietMealCrossRef("diet_medium", "lunch_3"),
            DietMealCrossRef("diet_medium", "lunch_4"),
            DietMealCrossRef("diet_medium", "lunch_6"),
            DietMealCrossRef("diet_medium", "afternoon_snack_3"),
            DietMealCrossRef("diet_medium", "afternoon_snack_4"),
            DietMealCrossRef("diet_medium", "dinner_3"),
            DietMealCrossRef("diet_medium", "dinner_5"),
            DietMealCrossRef("diet_medium", "optional_snack_2"),

            // Dieta Intensiva
            DietMealCrossRef("diet_intensive", "breakfast_3"),
            DietMealCrossRef("diet_intensive", "breakfast_8"),
            DietMealCrossRef("diet_intensive", "breakfast_10"),
            DietMealCrossRef("diet_intensive", "school_snack_4"),
            DietMealCrossRef("diet_intensive", "school_snack_6"),
            DietMealCrossRef("diet_intensive", "lunch_5"),
            DietMealCrossRef("diet_intensive", "lunch_7"),
            DietMealCrossRef("diet_intensive", "lunch_8"),
            DietMealCrossRef("diet_intensive", "afternoon_snack_1"),
            DietMealCrossRef("diet_intensive", "afternoon_snack_3"),
            DietMealCrossRef("diet_intensive", "dinner_4"),
            DietMealCrossRef("diet_intensive", "optional_snack_3"),

            // Dieta Vegetariana
            DietMealCrossRef("diet_vegetarian", "breakfast_1"),
            DietMealCrossRef("diet_vegetarian", "breakfast_4"),
            DietMealCrossRef("diet_vegetarian", "breakfast_9"),
            DietMealCrossRef("diet_vegetarian", "school_snack_1"),
            DietMealCrossRef("diet_vegetarian", "school_snack_4"),
            DietMealCrossRef("diet_vegetarian", "lunch_1"),
            DietMealCrossRef("diet_vegetarian", "lunch_5"),
            DietMealCrossRef("diet_vegetarian", "lunch_6"),
            DietMealCrossRef("diet_vegetarian", "afternoon_snack_1"),
            DietMealCrossRef("diet_vegetarian", "afternoon_snack_3"),
            DietMealCrossRef("diet_vegetarian", "dinner_1"),
            DietMealCrossRef("diet_vegetarian", "dinner_2"),
            DietMealCrossRef("diet_vegetarian", "optional_snack_2"),

            // Dieta para Estudiantes
            DietMealCrossRef("diet_student", "breakfast_5"),
            DietMealCrossRef("diet_student", "breakfast_6"),
            DietMealCrossRef("diet_student", "school_snack_2"),
            DietMealCrossRef("diet_student", "school_snack_3"),
            DietMealCrossRef("diet_student", "school_snack_5"),
            DietMealCrossRef("diet_student", "lunch_2"),
            DietMealCrossRef("diet_student", "lunch_7"),
            DietMealCrossRef("diet_student", "afternoon_snack_2"),
            DietMealCrossRef("diet_student", "afternoon_snack_4"),
            DietMealCrossRef("diet_student", "dinner_3"),
            DietMealCrossRef("diet_student", "dinner_4"),
            DietMealCrossRef("diet_student", "optional_snack_1"),

            // Dieta para Vida Activa
            DietMealCrossRef("diet_active", "breakfast_2"),
            DietMealCrossRef("diet_active", "breakfast_7"),
            DietMealCrossRef("diet_active", "breakfast_8"),
            DietMealCrossRef("diet_active", "school_snack_3"),
            DietMealCrossRef("diet_active", "school_snack_5"),
            DietMealCrossRef("diet_active", "lunch_2"),
            DietMealCrossRef("diet_active", "lunch_3"),
            DietMealCrossRef("diet_active", "lunch_4"),
            DietMealCrossRef("diet_active", "afternoon_snack_2"),
            DietMealCrossRef("diet_active", "afternoon_snack_3"),
            DietMealCrossRef("diet_active", "dinner_2"),
            DietMealCrossRef("diet_active", "dinner_4"),
            DietMealCrossRef("diet_active", "optional_snack_3")
        )
    }
}