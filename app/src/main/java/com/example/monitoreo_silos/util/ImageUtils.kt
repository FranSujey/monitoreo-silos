package com.example.monitoreo_silos.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ImageUriUtils {
    fun crearUriFotoTemporal(context: Context): Uri {
        val directorioFotos = File(context.getExternalFilesDir(null), "Pictures")
        if (!directorioFotos.exists()) {
            directorioFotos.mkdirs()
        }
        val archivoTemporal = File.createTempFile(
            "evidencia_silo_${System.currentTimeMillis()}",
            ".jpg",
            directorioFotos
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivoTemporal
        )
    }
}
