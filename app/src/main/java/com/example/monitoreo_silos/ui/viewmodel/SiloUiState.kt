package com.example.monitoreo_silos.ui.viewmodel

import com.example.monitoreo_silos.data.local.SiloEntity

sealed interface SiloUiState {
    object Loading : SiloUiState
    object Empty : SiloUiState
    data class Success(val silos: List<SiloEntity>) : SiloUiState
    data class Error(val message: String) : SiloUiState
}