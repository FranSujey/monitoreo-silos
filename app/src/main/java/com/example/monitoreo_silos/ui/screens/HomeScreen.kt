package com.example.monitoreo_silos.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.ui.viewmodel.SiloUiState
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel
import com.example.monitoreo_silos.ui.components.SiloCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SiloViewModel,
    usuario: String,
    onSiloClick: (SiloEntity) -> Unit,
    onCameraClick: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Monitoreo de Silos",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = usuario,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCameraClick) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Abrir cámara y galería"
                        )
                    }

                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
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

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(12.dp))

                        SummaryHeader(silos = state.silos)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Estado de Silos y Granjas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {

                            items(
                                items = state.silos,
                                key = { it.id }
                            ) { silo ->

                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(
                                        animationSpec = tween(300)
                                    ) + slideInVertically()
                                ) {

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
    }
}

@Composable
fun SummaryHeader(silos: List<SiloEntity>) {

    val totalGranjas = silos.map { it.granjaId }.distinct().size
    val totalSilos = silos.size
    val normales = silos.count { it.porcentajeLlenado > 30.0 }
    val advertencia = silos.count {
        it.porcentajeLlenado in 15.0..30.0
    }
    val criticos = silos.count {
        it.porcentajeLlenado < 15.0
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        "Granjas Registradas",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        "$totalGranjas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        "Silos Monitoreados",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        "$totalSilos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                BadgeEstado(
                    "Normal ($normales)",
                    Color(0xFF2E7D32)
                )

                BadgeEstado(
                    "Advertencia ($advertencia)",
                    Color(0xFFF57C00)
                )

                BadgeEstado(
                    "Crítico ($criticos)",
                    Color(0xFFC62828)
                )
            }
        }
    }
}

@Composable
fun BadgeEstado(
    texto: String,
    colorFondo: Color
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorFondo)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        Text(
            text = texto,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}