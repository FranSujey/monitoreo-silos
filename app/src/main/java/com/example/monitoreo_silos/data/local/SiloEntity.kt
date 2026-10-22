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
    val porcentajeLlenado: Double,
    val estado: String                // "Normal", "Advertencia", "Crítico"
)
