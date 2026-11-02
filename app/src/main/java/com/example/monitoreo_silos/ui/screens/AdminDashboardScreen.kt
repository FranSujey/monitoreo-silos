package com.example.monitoreo_silos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Logout
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    silos: List<SiloEntity>,
    onAgregarSilo: (SiloEntity) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var mostrarModalCrear by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel de Control - Supervisor",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarModalCrear = true },
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null)
                },
                text = {
                    Text("Agregar silo")
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

            Text(
                text = "Administración de silos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Supervisa la capacidad y el stock de las sucursales.",
                style = MaterialTheme.typography.bodyMedium
            )

            GraficoBarrasSilos(silos)

            Text(
                text = "Sucursales y Granjas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (silos.isEmpty()) {
                Text("Todavía no hay silos registrados.")
            }

            val sucursales = silos.groupBy { it.granjaId }

            sucursales.forEach { (granjaId, silosGranja) ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Store,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    "Sucursal Granja #$granjaId",
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    "${silosGranja.size} silos asignados",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Text(
                            text = "${silosGranja.sumOf { it.cantidadDisponible }.toInt()} Kg",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    if (mostrarModalCrear) {
        DialogoNuevoSilo(
            onDismiss = {
                mostrarModalCrear = false
            },
            onConfirm = { nuevoSilo ->
                onAgregarSilo(nuevoSilo)
                mostrarModalCrear = false
            }
        )
    }
}

@Composable
fun GraficoBarrasSilos(silos: List<SiloEntity>) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "Comparativo de Capacidad (%)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (silos.isEmpty()) {
                Text("No existen datos para mostrar.")
            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {

                    silos.forEach { silo ->

                        val altoPorcentaje =
                            (silo.porcentajeLlenado / 100.0)
                                .toFloat()
                                .coerceIn(0f, 1f)

                        val colorBarra = when {
                            silo.porcentajeLlenado < 20.0 ->
                                Color(0xFFC62828)

                            silo.porcentajeLlenado <= 30.0 ->
                                Color(0xFFF57C00)

                            else ->
                                Color(0xFF2E7D32)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {

                            Text(
                                "${silo.porcentajeLlenado.toInt()}%",
                                style = MaterialTheme.typography.labelSmall
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .fillMaxHeight(altoPorcentaje)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 6.dp,
                                            topEnd = 6.dp
                                        )
                                    )
                                    .background(colorBarra)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                "S-${silo.id}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Registrar Nuevo Silo")
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = granjaIdText,
                    onValueChange = {
                        granjaIdText = it
                        errorMensaje = null
                    },
                    label = {
                        Text("ID Sucursal / Granja")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = capacidadText,
                    onValueChange = {
                        capacidadText = it
                        errorMensaje = null
                    },
                    label = {
                        Text("Capacidad Total (Kg)")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tipoAlimentoText,
                    onValueChange = {
                        tipoAlimentoText = it
                        errorMensaje = null
                    },
                    label = {
                        Text("Tipo de Alimento")
                    },
                    singleLine = true
                )

                errorMensaje?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = {

                    val granjaId = granjaIdText.toIntOrNull()
                    val capacidad = capacidadText
                        .replace(",", ".")
                        .toDoubleOrNull()

                    val alimento = tipoAlimentoText.trim()

                    when {
                        granjaId == null || granjaId <= 0 -> {
                            errorMensaje = "Ingresa un ID de granja válido."
                        }

                        capacidad == null || capacidad <= 0 -> {
                            errorMensaje = "Ingresa una capacidad mayor que cero."
                        }

                        alimento.isBlank() -> {
                            errorMensaje = "Ingresa el tipo de alimento."
                        }

                        else -> {

                            val nuevoSilo = SiloEntity(
                                granjaId = granjaId,
                                capacidadTotal = capacidad,
                                cantidadDisponible = 0.0,
                                porcentajeLlenado = 0.0,
                                tipoAlimento = alimento,
                                estado = "Crítico"
                            )

                            onConfirm(nuevoSilo)
                        }
                    }
                }
            ) {
                Text("Crear Silo")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}