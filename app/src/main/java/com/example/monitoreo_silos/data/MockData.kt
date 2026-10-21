package com.example.monitoreo_silos.data

import com.example.monitoreo_silos.model.Silo

object MockData {
    val silosDePrueba = listOf(
        Silo(
            id = "SILO-001",
            nombreGranja = "Granja El Sol",
            capacidadTotalKg = 10000.0,
            nivelActualKg = 8500.0,
            fechaUltimaActualizacion = "2026-03-30 10:00"
        ),
        Silo(
            id = "SILO-002",
            nombreGranja = "Granja La Luna",
            capacidadTotalKg = 15000.0,
            nivelActualKg = 4000.0,
            fechaUltimaActualizacion = "2026-03-30 09:30"
        ),
        Silo(
            id = "SILO-003",
            nombreGranja = "Granja San José",
            capacidadTotalKg = 12000.0,
            nivelActualKg = 1200.0,
            fechaUltimaActualizacion = "2026-03-30 08:15"
        )
    )
}
