package com.example.monitoreo_silos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.data.local.SiloEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiloDetailScreen(
    silo: SiloEntity,
    onBackClick: () -> Unit,
    onRegistrarMovimiento: () -> Unit
) {
    val colorEstado = when {
        silo.porcentajeLlenado < 20.0 -> Color(0xFFC62828)
        silo.porcentajeLlenado <= 30.0 -> Color(0xFFF57C00)
        else -> Color(0xFF2E7D32)
    }

    val diasRestantes = (silo.cantidadDisponible / 50.0).toInt() // Estimación basada en 50kg/día

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Silo #${silo.id}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header con Ubicación y Estado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Granja / Sucursal #${silo.granjaId}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        AssistChip(
                            onClick = {},
                            label = { Text(silo.estado, color = Color.White) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = colorEstado)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Tipo de Alimento: ${silo.tipoAlimento}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Indicador de Nivel Gráfico
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nivel de Almacenamiento", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (silo.porcentajeLlenado / 100.0).toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(9.dp)),
                        color = colorEstado,
                        trackColor = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${silo.cantidadDisponible.toInt()} Kg Disponibles")
                        Text("${silo.porcentajeLlenado.toInt()}% Lleno", fontWeight = FontWeight.Bold, color = colorEstado)
                    }
                }
            }

            // Métricas de Consumo
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Capacidad Total", style = MaterialTheme.typography.labelMedium)
                        Text("${silo.capacidadTotal.toInt()} Kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Días Est. Restantes", style = MaterialTheme.typography.labelMedium)
                        Text("~$diasRestantes días", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Acción Directa
            Button(
                onClick = onRegistrarMovimiento,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar Carga o Descarga")
            }
        }
    }
}