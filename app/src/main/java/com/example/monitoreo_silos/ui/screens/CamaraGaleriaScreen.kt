package com.example.monitoreo_silos.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.monitoreo_silos.ui.viewmodel.CamaraGaleriaViewModel

@Composable
fun CamaraGaleriaScreen(
    viewModel: CamaraGaleriaViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.imagenDesdeCamara(bitmap)
        }
    }
    val galeria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.imagenDesdeGaleria(uri)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Cámara y galería",
            style = MaterialTheme.typography.headlineMedium
        )
        Button(
            onClick = { camara.launch(null) }
        ) {
            Text("Tomar fotografía")
        }
        Button(
            onClick = { galeria.launch("image/*") }
        ) {
            Text("Seleccionar imagen")
        }
        when {
            estado.bitmap != null -> {
                Image(
                    bitmap = estado.bitmap!!.asImageBitmap(),
                    contentDescription = "Fotografía tomada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Fit
                )
            }

            estado.uri != null -> {
                AsyncImage(
                    model = estado.uri,
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Fit
                )
            }

            else -> {
                Text("No hay ninguna imagen seleccionada")
            }
        }
        if (estado.origen.isNotEmpty()) {
            Text("Imagen obtenida desde: ${estado.origen}")
            OutlinedButton(
                onClick = viewModel::limpiarImagen
            ) {
                Text("Eliminar imagen")
            }
        }
    }
}
