package com.example.monitoreo_silos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.ui.components.SiloCard
import com.example.monitoreo_silos.ui.viewmodel.SiloUiState
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SiloViewModel,
    onSiloClick: (SiloEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monitoreo de Silos", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is SiloUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is SiloUiState.Empty -> {
                    Text(
                        text = "No hay silos cargados en la base de datos.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is SiloUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is SiloUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.silos,
                            key = { it.id }
                        ) { silo ->
                            SiloCard(
                                silo = silo,
                                onSiloClick = { siloSeleccionado ->
                                    onSiloClick(siloSeleccionado)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}