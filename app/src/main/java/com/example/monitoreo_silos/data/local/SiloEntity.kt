package com.example.monitoreo_silos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "silos")
data class SiloEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val granjaId: Int,
    val capacidadTotal: Double,       // Toneladas o kg
    val cantidadDisponible: Double,   // Cantidad actual
    val tipoAlimento: String,         // "Iniciador", "Engorda"
    val estado: String                // "Normal", "Advertencia", "Crítico"
) {
    // Propiedad calculada helper para obtener el porcentaje en tiempo real
    val porcentajeLlenado: Double
        get() = if (capacidadTotal > 0) (cantidadDisponible / capacidadTotal) * 100.0 else 0.0
}