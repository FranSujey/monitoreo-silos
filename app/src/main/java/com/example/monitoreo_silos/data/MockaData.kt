package com.example.monitoreo_silos.data
import com.example.monitoreo_silos.modeldata.Silo

object MockData {
    val silosDePrueba = listOf(
        Silo("Silo A1", "Granja Melipilla Sur", 10000.0, 8500.0, "2026-08-31 08:30"),
        Silo("Silo A2", "Granja Melipilla Sur", 10000.0, 3000.0, "2026-08-31 09:00"), // Advertencia
        Silo("Silo B1", "Granja El Paico", 15000.0, 1200.0, "2026-08-31 07:45")      // Crítico
    )
}