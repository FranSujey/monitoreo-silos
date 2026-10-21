package com.example.monitoreo_silos.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Si el registro fue exitoso, volver
    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess) {
            onGuardarExitoso()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registrar Movimiento - Silo #${silo.id}") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Capacidad Actual: ${silo.cantidadDisponible} / ${silo.capacidadTotal} Ton",
                style = MaterialTheme.typography.bodyMedium
            )

            // Selección de Tipo de Movimiento (Carga o Descarga)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = formState.tipoMovimiento == "Carga",
                    onClick = { viewModel.onTipoMovimientoChanged("Carga", silo) }
                )
                Text("Carga de Alimento")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = formState.tipoMovimiento == "Descarga",
                    onClick = { viewModel.onTipoMovimientoChanged("Descarga", silo) }
                )
                Text("Descarga / Consumo")
            }

            // Campo 1: Cantidad con Retroalimentación Visual e Ícono de Error
            OutlinedTextField(
                value = formState.cantidadText,
                onValueChange = { viewModel.onCantidadChanged(it, silo) },
                label = { Text("Cantidad (Toneladas)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.cantidadError != null,
                trailingIcon = {
                    if (formState.cantidadError != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error de validación",
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
                        Text("Ingresa una cantidad mayor a 0 y que no supere la capacidad.")
                    }
                }
            )

            // Campo 2: Observaciones e Incidencias
            OutlinedTextField(
                value = formState.observacionesText,
                onValueChange = { viewModel.onObservacionesChanged(it) },
                label = { Text("Observaciones / Reporte de Incidencias") },
                modifier = Modifier.fillMaxWidth(),
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

            // Sección de evidencia fotográfica
            SeccionEvidenciaFoto(
                fotoUri = fotoEvidenciaUri,
                onFotoSeleccionada = { uri ->
                    fotoEvidenciaUri = uri
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón de Enviar (Solo habilitado si el formulario es válido)
            Button(
                onClick = { viewModel.guardarMovimiento(silo.id) },
                enabled = formState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar y Guardar Registro")
            }
        }
    }
}
