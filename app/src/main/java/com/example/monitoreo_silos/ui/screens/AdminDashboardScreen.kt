package com.example.monitoreo_silos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    silos: List<SiloEntity>,
    onAgregarSilo: (SiloEntity) -> Unit
) {
    var mostrarModalCrear by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Control General", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarModalCrear = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Silo", tint = Color.White)
            }
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
            // 1. Gráfico Visual de Barras
            GraficoBarrasSilos(silos = silos)

            // 2. Resumen por Sucursales
            Text(text = "Sucursales y Granjas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            val sucursales = silos.groupBy { it.granjaId }

            sucursales.forEach { (granjaId, silosGranja) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Store, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Sucursal Granja #$granjaId", fontWeight = FontWeight.Bold)
                                Text("${silosGranja.size} Silos Asignados", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Text(
                            text = "${silosGranja.sumOf { it.cantidadDisponible }.toInt()} Kg Total",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    // Modal para agregar Silo
    if (mostrarModalCrear) {
        DialogoNuevoSilo(
            onDismiss = { mostrarModalCrear = false },
            onConfirm = { nuevoSilo ->
                onAgregarSilo(nuevoSilo)
                mostrarModalCrear = false
            }
        )
    }
}

@Composable
fun GraficoBarrasSilos(silos: List<SiloEntity>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.BarChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Comparativo de Capacidad (%)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                silos.forEach { silo ->
                    val altoPorcentaje = (silo.porcentajeLlenado / 100.0).toFloat()
                    val colorBarra = when {
                        silo.porcentajeLlenado < 20.0 -> Color(0xFFC62828)
                        silo.porcentajeLlenado <= 30.0 -> Color(0xFFF57C00)
                        else -> Color(0xFF2E7D32)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text("${silo.porcentajeLlenado.toInt()}%", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .fillMaxHeight(altoPorcentaje)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(colorBarra)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("S-#${silo.id}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DialogoNuevoSilo(
    onDismiss: () -> Unit,
    onConfirm: (SiloEntity) -> Unit
) {
    var granjaIdText by remember { mutableStateOf("") }
    var capacidadText by remember { mutableStateOf("") }
    var tipoAlimentoText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Nuevo Silo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = granjaIdText,
                    onValueChange = { granjaIdText = it },
                    label = { Text("ID Sucursal / Granja") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = capacidadText,
                    onValueChange = { capacidadText = it },
                    label = { Text("Capacidad Total (Kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = tipoAlimentoText,
                    onValueChange = { tipoAlimentoText = it },
                    label = { Text("Tipo de Alimento") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val gId = granjaIdText.toIntOrNull() ?: 1
                    val cap = capacidadText.toDoubleOrNull() ?: 1000.0
                    onConfirm(
                        SiloEntity(
                            granjaId = gId,
                            capacidadTotal = cap,
                            cantidadDisponible = cap,
                            porcentajeLlenado = 100.0,
                            tipoAlimento = tipoAlimentoText.ifBlank { "Trigo" },
                            estado = "Normal"
                        )
                    )
                }
            ) { Text("Crear Silo") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}