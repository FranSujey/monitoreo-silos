package com.example.monitoreo_silos.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraGaleriaViewModel : ViewModel() {

    // Lista de URIs de imagenes seleccionadas/capturadas
    private val _imagenesSeleccionadas = MutableStateFlow<List<Uri>>(emptyList())
    val imagenesSeleccionadas: StateFlow<List<Uri>> = _imagenesSeleccionadas.asStateFlow()

    // URI temporal para la foto que se va a tomar con la cámara
    private val _tempCameraUri = MutableStateFlow<Uri?>(null)
    val tempCameraUri: StateFlow<Uri?> = _tempCameraUri.asStateFlow()

    fun setTempCameraUri(uri: Uri?) {
        _tempCameraUri.value = uri
    }

    fun agregarImagen(uri: Uri) {
        _imagenesSeleccionadas.value = _imagenesSeleccionadas.value + uri
    }

    fun eliminarImagen(uri: Uri) {
        _imagenesSeleccionadas.value = _imagenesSeleccionadas.value - uri
    }
}