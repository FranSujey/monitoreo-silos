package com.example.monitoreo_silos.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.ui.components.SeccionEvidenciaFoto
import com.example.monitoreo_silos.ui.viewmodel.FormMovimientoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMovimientoScreen(
    silo: SiloEntity,
    viewModel: FormMovimientoViewModel,
    onGuardarExitoso: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    var fotoEvidenciaUri by remember { mutableStateOf<Uri?>(null) }

    // Retorno automático tras registro exitoso
    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess) {
            onGuardarExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Movimiento - Silo #${silo.id}",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onGuardarExitoso) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
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
            // Tarjeta Informativa de Capacidad Actual
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estado Actual del Silo",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Disponible: ${silo.cantidadDisponible} / ${silo.capacidadTotal} Ton",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tipo de alimento: ${silo.tipoAlimento}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Selector de Tipo de Movimiento (Carga o Descarga)
            Text(
                text = "Tipo de Operación",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = formState.tipoMovimiento == "Carga",
                    onClick = { viewModel.onTipoMovimientoChanged("Carga", silo) },
                    label = { Text("Carga de Alimento") },
                    leadingIcon = {
                        if (formState.tipoMovimiento == "Carga") {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                FilterChip(
                    selected = formState.tipoMovimiento == "Descarga",
                    onClick = { viewModel.onTipoMovimientoChanged("Descarga", silo) },
                    label = { Text("Descarga / Consumo") },
                    leadingIcon = {
                        if (formState.tipoMovimiento == "Descarga") {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                        }
                    }
                )
            }

            // Campo 1: Cantidad con Validaciones en Tiempo Real
            OutlinedTextField(
                value = formState.cantidadText,
                onValueChange = { viewModel.onCantidadChanged(it, silo) },
                label = { Text("Cantidad (Toneladas / Kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.cantidadError != null,
                trailingIcon = {
                    if (formState.cantidadError != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                supportingText = {
                    if (formState.cantidadError != null) {
                        Text(
                            text = formState.cantidadError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text("Ingrese un valor superior a 0 dentro de la capacidad disponible.")
                    }
                }
            )

            // Campo 2: Reporte de Incidencias / Observaciones
            OutlinedTextField(
                value = formState.observacionesText,
                onValueChange = { viewModel.onObservacionesChanged(it) },
                label = { Text("Observaciones / Reporte de Incidencias") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                isError = formState.observacionesError != null,
                supportingText = {
                    if (formState.observacionesError != null) {
                        Text(
                            text = formState.observacionesError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // Componente de Cámara Nativa / Galería de Evidencia
            SeccionEvidenciaFoto(
                fotoUri = fotoEvidenciaUri,
                onFotoSeleccionada = { uri ->
                    fotoEvidenciaUri = uri
                }
            )

            // REINICIAR FORMULARIO AL ABRIR UN SILO
            LaunchedEffect(silo.id) {
                viewModel.iniciarFormulario(silo.id)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de Envío con Animación de Retroalimentación Visual
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Button(
                    onClick = { viewModel.guardarMovimiento(silo.id) },
                    enabled = formState.isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Confirmar y Guardar Registro",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}