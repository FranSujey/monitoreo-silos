package com.example.monitoreo_silos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "silos")
data class SiloEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val granjaId: Int,
    val capacidadTotal: Double,
    val cantidadDisponible: Double,
    val tipoAlimento: String,
    val porcentajeLlenado: Double,
    val estado: String
)