package com.example.monitoreo_silos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.monitoreo_silos.data.local.AppDatabase
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.data.repository.SiloRepository
import com.example.monitoreo_silos.ui.screens.FormMovimientoScreen
import com.example.monitoreo_silos.ui.screens.HomeScreen
import com.example.monitoreo_silos.ui.theme.MonitoreoSilosTheme
import com.example.monitoreo_silos.ui.viewmodel.FormMovimientoViewModel
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar Persistencia Local SQLite / Room y Repositorio
        val database = AppDatabase.getDatabase(this)
        val repository = SiloRepository(database.siloDao())

        // 2. Instanciar ViewModels
        val homeViewModel = SiloViewModel(repository)
        val formViewModel = FormMovimientoViewModel(repository)

        setContent {
            MonitoreoSilosTheme {
                // Estado de navegación reactiva
                var siloSeleccionado by remember { mutableStateOf<SiloEntity?>(null) }

                if (siloSeleccionado == null) {
                    // Pantalla 1: Monitoreo Principal conectado a Room DB
                    HomeScreen(
                        viewModel = homeViewModel,
                        onSiloClick = { silo ->
                            siloSeleccionado = silo
                        }
                    )
                } else {
                    // Pantalla 2: Formulario con Validaciones + Cámara Nativa + Persistencia
                    FormMovimientoScreen(
                        silo = siloSeleccionado!!,
                        viewModel = formViewModel,
                        onGuardarExitoso = {
                            siloSeleccionado = null // Regresa a la lista principal tras guardar
                        }
                    )
                }
            }
        }
    }
}