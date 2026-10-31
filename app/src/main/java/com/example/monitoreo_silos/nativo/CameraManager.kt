package com.example.monitoreo_silos.nativo

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraManager(private val context: Context) {

    // Genera un archivo de imagen temporal y devuelve su URI pública mediante FileProvider
    fun crearArchivoFotoUri(): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreArchivo = "EVIDENCIA_SILO_${timeStamp}_"
        val directorioAlmacenamiento: File? = context.getExternalFilesDir(null)

        val archivoImagen = File.createTempFile(
            nombreArchivo, /* prefijo */
            ".jpg",        /* sufijo */
            directorioAlmacenamiento /* directorio */
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivoImagen
        )
    }
}