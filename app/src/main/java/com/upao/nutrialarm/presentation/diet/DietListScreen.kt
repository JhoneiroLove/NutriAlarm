package com.upao.nutrialarm.presentation.diet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.upao.nutrialarm.domain.model.AnemiaRisk
import com.upao.nutrialarm.domain.model.Diet
import com.upao.nutrialarm.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietListScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: DietViewModel = hiltViewModel()
) {
    val diets by viewModel.diets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Dietas Antianoemia") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = NutriBlue,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NutriBlue)
            }
        } else if (error != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = error!!,
                    color = NutriRed,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.loadDiets() },
                    colors = ButtonDefaults.buttonColors(containerColor = NutriBlue)
                ) {
                    Text("Reintentar")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Selecciona la dieta adecuada según tu nivel de riesgo de anemia",
                        fontSize = 16.sp,
                        color = NutriGrayDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(diets) { diet ->
                    DietCard(
                        diet = diet,
                        onClick = {
                            // TODO: Navegar al detalle de la dieta
                        }
                    )
                }

                if (diets.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = NutriGrayLight)
                        ) {
                            Text(
                                text = "No hay dietas disponibles",
                                modifier = Modifier.padding(20.dp),
                                fontSize = 16.sp,
                                color = NutriGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietCard(
    diet: Diet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = when (diet.anemiaRiskLevel) {
                AnemiaRisk.LOW -> NutriGreen.copy(alpha = 0.1f)
                AnemiaRisk.MEDIUM -> NutriOrange.copy(alpha = 0.1f)
                AnemiaRisk.HIGH -> NutriRed.copy(alpha = 0.1f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = diet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark,
                    modifier = Modifier.weight(1f)
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (diet.anemiaRiskLevel) {
                            AnemiaRisk.LOW -> NutriGreen
                            AnemiaRisk.MEDIUM -> NutriOrange
                            AnemiaRisk.HIGH -> NutriRed
                        }
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = when (diet.anemiaRiskLevel) {
                            AnemiaRisk.LOW -> "RIESGO BAJO"
                            AnemiaRisk.MEDIUM -> "RIESGO MEDIO"
                            AnemiaRisk.HIGH -> "RIESGO ALTO"
                        },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = diet.description,
                fontSize = 14.sp,
                color = NutriGray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientChip(
                    label = "Hierro",
                    value = "${diet.ironContent} mg",
                    color = IronRed
                )

                NutrientChip(
                    label = "Calorías",
                    value = "${diet.calories.toInt()} cal",
                    color = NutriBlue
                )
            }
        }
    }
}

@Composable
fun NutrientChip(
    label: String,
    value: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = color,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}