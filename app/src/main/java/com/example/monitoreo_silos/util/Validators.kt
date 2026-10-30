package com.example.monitoreo_silos.util

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

object FormValidators {
    fun validarCantidad(valor: String, capacidadMax: Double): ValidationResult {
        val numero = valor.toDoubleOrNull()
            ?: return ValidationResult.Error("Debe ingresar un número válido")
        if (numero <= 0) {
            return ValidationResult.Error("La cantidad debe ser mayor a 0")
        }
        if (numero > capacidadMax) {
            return ValidationResult.Error("Supera la capacidad máxima del silo ($capacidadMax kg)")
        }
        return ValidationResult.Success
    }

    fun validarObservacion(texto: String): ValidationResult {
        if (texto.isBlank()) {
            return ValidationResult.Error("La observación o incidencia no puede estar vacía")
        }
        if (texto.length < 10) {
            return ValidationResult.Error("Escriba un detalle de al menos 10 caracteres")
        }
        return ValidationResult.Success
    }
}