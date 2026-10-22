package com.example.monitoreo_silos.model
enum class EstadoSilo {
    NORMAL,
    ADVERTENCIA,
    CRITICO
}

class Silo(
    val id: String,
    val nombreGranja: String,
    val capacidadTotalKg: Double,
    val nivelActualKg: Double,
    val fechaUltimaActualizacion: String
) {
    val porcentajeDisponible: Int
        get() = ((nivelActualKg / capacidadTotalKg) * 100).toInt()

    val estado: EstadoSilo
        get() = when {
            porcentajeDisponible <= 15 -> EstadoSilo.CRITICO
            porcentajeDisponible <= 35 -> EstadoSilo.ADVERTENCIA
            else -> EstadoSilo.NORMAL
        }
}