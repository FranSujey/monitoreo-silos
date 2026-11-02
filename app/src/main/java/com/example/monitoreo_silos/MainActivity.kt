package com.example.monitoreo_silos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.monitoreo_silos.data.local.AppDatabase
import com.example.monitoreo_silos.data.local.UserEntity
import com.example.monitoreo_silos.data.repository.SiloRepository
import com.example.monitoreo_silos.navigation.AppNavigation
import com.example.monitoreo_silos.repository.AuthRepository
import com.example.monitoreo_silos.ui.AuthViewModel
import com.example.monitoreo_silos.ui.theme.MonitoreoSilosTheme
import com.example.monitoreo_silos.ui.viewmodel.FormMovimientoViewModel
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)

        val siloRepository = SiloRepository(database.siloDao())
        val authRepository = AuthRepository(database.userDao())

        // Registrar usuario administrador si todavía no existe.
        lifecycleScope.launch {
            authRepository.register(
                UserEntity(
                    username = "admin",
                    passwordHash = "1234",
                    nombre = "Carlos Perez (Supervisor)",
                    rol = "SUPERVISOR"
                )
            )
        }

        val siloViewModel = SiloViewModel(siloRepository)
        val formMovimientoViewModel = FormMovimientoViewModel(siloRepository)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
            MonitoreoSilosTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    siloViewModel = siloViewModel,
                    formMovimientoViewModel = formMovimientoViewModel
                )
            }
        }
    }
}