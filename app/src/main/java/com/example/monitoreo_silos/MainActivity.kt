package com.example.monitoreo_silos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.monitoreo_silos.data.local.AppDatabase
import com.example.monitoreo_silos.data.local.SiloEntity
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

        // 1. Inicializar Persistencia Local SQLite / Room y Repositorios
        val database = AppDatabase.getDatabase(this)
        val siloRepository = SiloRepository(database.siloDao())
        val authRepository = AuthRepository(database.userDao())

        // 2. Precarga de datos iniciales (Seed Data)
        lifecycleScope.launch {
            // Usuario de prueba predeterminado
            authRepository.register(
                UserEntity(
                    username = "admin",
                    passwordHash = "1234",
                    nombre = "Carlos Perez (Supervisor)",
                    rol = "SUPERVISOR"
                )
            )

            // Silos de prueba con coincidencia exacta de parámetros de SiloEntity
            siloRepository.insertarSilo(
                SiloEntity(
                    id = 1,
                    granjaId = 101,
                    capacidadTotal = 1000.0,
                    cantidadDisponible = 850.0,
                    tipoAlimento = "Iniciador",
                    porcentajeLlenado = 85.0,
                    estado = "Normal"
                )
            )
            siloRepository.insertarSilo(
                SiloEntity(
                    id = 2,
                    granjaId = 101,
                    capacidadTotal = 1000.0,
                    cantidadDisponible = 250.0,
                    tipoAlimento = "Engorda",
                    porcentajeLlenado = 25.0,
                    estado = "Advertencia"
                )
            )
            siloRepository.insertarSilo(
                SiloEntity(
                    id = 3,
                    granjaId = 102,
                    capacidadTotal = 1000.0,
                    cantidadDisponible = 100.0,
                    tipoAlimento = "Engorda",
                    porcentajeLlenado = 10.0,
                    estado = "Crítico"
                )
            )
        }

        // 3. Instanciar ViewModels
        val homeViewModel = SiloViewModel(siloRepository)
        val formViewModel = FormMovimientoViewModel(siloRepository)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
            MonitoreoSilosTheme {
                // 4. Cargar la navegación general que inicia en LoginScreen
                AppNavigation(
                    authViewModel = authViewModel,
                    siloViewModel = homeViewModel,
                    formMovimientoViewModel = formViewModel
                )
            }
        }
    }
}