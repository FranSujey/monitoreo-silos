package com.example.monitoreo_silos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.data.repository.SiloRepository
import com.example.monitoreo_silos.ui.state.FormMovimientoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormMovimientoViewModel(
    private val repository: SiloRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(FormMovimientoUiState())
    val formState: StateFlow<FormMovimientoUiState> = _formState.asStateFlow()

    // Manejar cambio en el campo de cantidad con validaciones
    fun onCantidadChanged(nuevaCantidad: String, silo: SiloEntity?) {
        val cantidadDouble = nuevaCantidad.toDoubleOrNull()

        // Reglas de negocio para validación
        val errorMsg = when {
            nuevaCantidad.isBlank() -> "La cantidad es obligatoria."
            cantidadDouble == null -> "Debes ingresar un valor numérico válido."
            cantidadDouble <= 0.0 -> "La cantidad debe ser mayor a cero."

            // Validación de capacidad en caso de Carga
            _formState.value.tipoMovimiento == "Carga" && silo != null -> {
                val espacioDisponible = silo.capacidadTotal - silo.cantidadDisponible
                if (cantidadDouble > espacioDisponible) {
                    "La carga excede el espacio disponible (${String.format("%.1f", espacioDisponible)} Ton máx)."
                } else null
            }

            // Validación de stock en caso de Descarga
            _formState.value.tipoMovimiento == "Descarga" && silo != null -> {
                if (cantidadDouble > silo.cantidadDisponible) {
                    "La descarga excede el alimento disponible (${silo.cantidadDisponible} Ton máx)."
                } else null
            }

            else -> null
        }

        _formState.update { currentState ->
            val isValido = errorMsg == null && currentState.observacionesError == null && nuevaCantidad.isNotBlank()
            currentState.copy(
                cantidadText = nuevaCantidad,
                cantidadError = errorMsg,
                isFormValid = isValido
            )
        }
    }

    // Manejar cambio en observaciones
    fun onObservacionesChanged(texto: String) {
        _formState.update { currentState ->
            currentState.copy(
                observacionesText = texto,
                observacionesError = null
            )
        }
    }

    // Cambiar tipo de movimiento (Carga / Descarga)
    fun onTipoMovimientoChanged(nuevoTipo: String, silo: SiloEntity?) {
        _formState.update { it.copy(tipoMovimiento = nuevoTipo) }
        // Re-validar la cantidad con las reglas del nuevo tipo
        onCantidadChanged(_formState.value.cantidadText, silo)
    }

    // Guardar el registro en la base de datos local
    fun guardarMovimiento(siloId: Int) {
        viewModelScope.launch {
            val cantidad = _formState.value.cantidadText.toDoubleOrNull() ?: return@launch
            val exito = if (_formState.value.tipoMovimiento == "Carga") {
                repository.registrarCarga(siloId, cantidad)
            } else {
                repository.registrarDescarga(siloId, cantidad)
            }

            if (exito) {
                _formState.update { it.copy(isSuccess = true) }
            }
        }
    }
}