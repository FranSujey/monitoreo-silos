package com.example.monitoreo_silos.model
enum class TipoMovimiento {
    CARGA,
    DESCARGA,
    INCIDENCIA
}

data class Movimiento(
    val id: Int = 0,
    val siloId: Int,
    val tipo: TipoMovimiento,
    val cantidadKg: Double,
    val fecha: String,
    val observacion: String = "",
    val fotoUri: String? = null // Ruta o URI de la imagen obtenida con la cámara/galería
)