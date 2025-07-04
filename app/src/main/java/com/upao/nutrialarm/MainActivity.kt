package com.upao.nutrialarm

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.upao.nutrialarm.data.local.database.DatabaseInitializer
import com.upao.nutrialarm.data.local.preferences.PreferencesManager
import com.upao.nutrialarm.domain.repository.DietRepository
import com.upao.nutrialarm.presentation.navigation.NavigationGraph
import com.upao.nutrialarm.ui.theme.NutriAlarmTheme
import com.upao.nutrialarm.util.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dietRepository: DietRepository

    @Inject
    lateinit var permissionHelper: PermissionHelper

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    @Inject
    lateinit var preferencesManager: PreferencesManager

    // Launcher para permisos de notificación
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("MainActivity", "Notification permission granted")
        } else {
            Log.w("MainActivity", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Solicitar permisos necesarios
        checkAndRequestPermissions()

        setContent {
            NutriAlarmTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    LaunchedEffect(Unit) {
                        try {
                            // Inicializar base de datos
                            databaseInitializer.initializeDatabase()

                            // Verificar integridad de datos
                            val integrityResult = databaseInitializer.verifyDataIntegrity()
                            Log.d("MainActivity", "Data integrity: ${integrityResult}")

                            if (integrityResult.isValid) {
                                preferencesManager.isFirstLaunch = false
                            }

                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error during initialization", e)
                            // Fallback: usar el método anterior
                            dietRepository.initializePreloadedData()
                        }
                    }

                    NavigationGraph()
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionStatus = permissionHelper.checkAllPermissions()

        // Solicitar permiso de notificaciones si no lo tiene
        if (!permissionStatus.hasNotifications) {
            permissionHelper.requestNotificationPermission(this)
        }

        // Solicitar permiso de alarmas exactas si no lo tiene
        if (!permissionStatus.hasExactAlarms) {
            permissionHelper.requestExactAlarmPermission(this)
        }

        Log.d("MainActivity", "Permissions status: $permissionStatus")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionHelper.NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Notification permission granted")
                } else {
                    Log.w("MainActivity", "Notification permission denied")
                }
            }
        }
    }
}