package com.example.monitoreo_silos.model

data class Granja(
    val id: Int,
    val nombre: String,
    val ubicacion: String = "",
    val encargado: String = ""
)