package com.example.monitoreo_silos.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
data class ImagenUiState(
    val bitmap: Bitmap? = null,
    val uri: Uri? = null,
    val origen: String = ""
)
class CamaraGaleriaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ImagenUiState())
    val uiState = _uiState.asStateFlow()
    fun imagenDesdeCamara(bitmap: Bitmap?) {
        _uiState.value = ImagenUiState(
            bitmap = bitmap,
            origen = if (bitmap != null) "Cámara" else ""
        )
    }
    fun imagenDesdeGaleria(uri: Uri?) {
        _uiState.value = ImagenUiState(
            uri = uri,
            origen = if (uri != null) "Galería" else ""
        )
    }
    fun limpiarImagen() {
        _uiState.value = ImagenUiState()
    }
    }