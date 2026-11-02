package com.example.monitoreo_silos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoreo_silos.data.local.SiloEntity
import com.example.monitoreo_silos.data.repository.SiloRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SiloViewModel(private val repository: SiloRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Conversión reactiva del Flow de Room a SiloUiState
    val uiState: StateFlow<SiloUiState> = repository.obtenerTodosLosSilos()
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SiloUiState.Loading
        )

    init {
        verificarYInicializarDatos()
    }

    // VERIFICAR E INICIALIZAR DATOS EN LA BASE LOCAL
    private fun verificarYInicializarDatos() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val listaActual = repository
                    .obtenerTodosLosSilos()
                    .firstOrNull() ?: emptyList()

                if (listaActual.isEmpty()) {
                    repository.insertarSilosIniciales()
                }

            } catch (e: Exception) {
                e.printStackTrace()

            } finally {
                _isLoading.value = false
            }
        }
    }

    // NUEVO MÉTODO: Permite insertar un nuevo silo desde la interfaz o el panel de admin
    fun insertarSilo(silo: SiloEntity) {
        viewModelScope.launch {
            repository.insertarSilo(silo)
        }
    }
}