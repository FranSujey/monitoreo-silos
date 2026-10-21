package com.example.monitoreo_silos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoreo_silos.data.repository.SiloRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SiloViewModel(private val repository: SiloRepository) : ViewModel() {

    // Conversión del Flow de Room a StateFlow de UI con manejo de estados
    val uiState: StateFlow<SiloUiState> = repository.allSilos
        .map { listaSilos ->
            if (listaSilos.isEmpty()) {
                SiloUiState.Empty
            } else {
                SiloUiState.Success(silos = listaSilos)
            }
        }
        .catch { error ->
            emit(SiloUiState.Error(message = error.localizedMessage ?: "Error desconocido"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Detiene la recolección tras 5s de inactividad
            initialValue = SiloUiState.Loading
        )
}