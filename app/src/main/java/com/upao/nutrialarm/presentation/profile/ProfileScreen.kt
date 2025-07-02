package com.upao.nutrialarm.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upao.nutrialarm.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Mi Perfil") },
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Información del usuario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NutriBlue.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = NutriBlue,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Usuario Demo", // TODO: Obtener del ViewModel
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NutriGrayDark
                        )

                        Text(
                            text = "usuario@email.com", // TODO: Obtener del ViewModel
                            fontSize = 14.sp,
                            color = NutriGray
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriGrayDark,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                ProfileMenuItem(
                    title = "Datos Personales",
                    subtitle = "Edad, peso, altura, nivel de actividad",
                    icon = Icons.Default.Person,
                    onClick = {
                        // TODO: Navegar a editar perfil
                    }
                )
            }

            item {
                ProfileMenuItem(
                    title = "Configurar Alarmas",
                    subtitle = "Horarios de comidas y recordatorios",
                    icon = Icons.Default.Notifications,
                    onClick = {
                        // TODO: Navegar a configurar alarmas
                    }
                )
            }

            item {
                ProfileMenuItem(
                    title = "Estado de Salud",
                    subtitle = "Nivel de riesgo de anemia",
                    icon = Icons.Default.Favorite,
                    onClick = {
                        // TODO: Navegar a estado de salud
                    }
                )
            }

            item {
                ProfileMenuItem(
                    title = "Configuración",
                    subtitle = "Preferencias de la aplicación",
                    icon = Icons.Default.Settings,
                    onClick = {
                        // TODO: Navegar a configuración
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // TODO: Implementar logout
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NutriRed)
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = NutriBlue,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = NutriGrayDark
                )

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = NutriGray
                )
            }
        }
    }
}