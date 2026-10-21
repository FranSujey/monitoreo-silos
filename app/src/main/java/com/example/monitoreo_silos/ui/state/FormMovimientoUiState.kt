package com.example.monitoreo_silos.ui.state

data class FormMovimientoUiState(
    val siloId: Int = 0,
    val cantidadText: String = "",
    val cantidadError: String? = null,
    val tipoMovimiento: String = "Carga", // "Carga" o "Descarga"
    val observacionesText: String = "",
    val observacionesError: String? = null,
    val isFormValid: Boolean = false,
    val isSuccess: Boolean = false
)