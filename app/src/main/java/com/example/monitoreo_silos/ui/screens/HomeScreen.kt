package com.example.monitoreo_silos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.ui.components.SiloCard
import com.example.monitoreo_silos.ui.viewmodel.SiloUiState
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SiloViewModel,
    onSiloClick: (Int) -> Unit
) {
    // Observar el StateFlow desde Compose
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monitoreo de Silos") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Estructura de control implícita para alternar respuesta visual
            when (val state = uiState) {
                is SiloUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Cargando estado de silos...")
                    }
                }

                is SiloUiState.Empty -> {
                    Text(
                        text = "No hay registros de silos disponibles.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is SiloUiState.Error -> {
                    Text(
                        text = "Error al cargar datos: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is SiloUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.silos) { silo ->
                            SiloCard(
                                silo = silo,
                                onSiloClick = onSiloClick
                            )
                        }
                    }
                }
            }
        }
    }
}