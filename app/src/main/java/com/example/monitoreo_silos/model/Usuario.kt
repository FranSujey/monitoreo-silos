package com.example.monitoreo_silos.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String = "Operador de Planta"
)