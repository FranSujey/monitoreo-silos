package com.example.monitoreo_silos.data.repository

import com.example.monitoreo_silos.data.local.SiloDao
import com.example.monitoreo_silos.data.local.SiloEntity
import kotlinx.coroutines.flow.Flow

class SiloRepository(private val siloDao: SiloDao) {

    // Flujo continuo para observar la lista completa de silos
    val allSilos: Flow<List<SiloEntity>> = siloDao.getAllSilos()

    fun obtenerTodosLosSilos(): Flow<List<SiloEntity>> = siloDao.getAllSilos()

    suspend fun insertSilo(silo: SiloEntity) {
        siloDao.insertSilo(silo)
    }

    suspend fun actualizarSilo(silo: SiloEntity) {
        siloDao.updateSilo(silo)
    }

    // Registrar Carga de Alimento (Suma stock y recalculación de porcentaje y estado)
    suspend fun registrarCarga(siloId: Int, cantidadIngresada: Double): Boolean {
        val silo = siloDao.getSiloById(siloId) ?: return false
        val nuevaCantidad = (silo.cantidadDisponible + cantidadIngresada).coerceAtMost(silo.capacidadTotal)
        val nuevoPorcentaje = if (silo.capacidadTotal > 0) (nuevaCantidad / silo.capacidadTotal) * 100.0 else 0.0
        val nuevoEstado = calcularEstado(nuevaCantidad, silo.capacidadTotal)

        val siloActualizado = silo.copy(
            cantidadDisponible = nuevaCantidad,
            porcentajeLlenado = nuevoPorcentaje,
            estado = nuevoEstado
        )
        siloDao.updateSilo(siloActualizado)
        return true
    }

    // Registrar Descarga de Alimento (Resta stock y recalculación de porcentaje y estado)
    suspend fun registrarDescarga(siloId: Int, cantidadRetirada: Double): Boolean {
        val silo = siloDao.getSiloById(siloId) ?: return false
        val nuevaCantidad = (silo.cantidadDisponible - cantidadRetirada).coerceAtLeast(0.0)
        val nuevoPorcentaje = if (silo.capacidadTotal > 0) (nuevaCantidad / silo.capacidadTotal) * 100.0 else 0.0
        val nuevoEstado = calcularEstado(nuevaCantidad, silo.capacidadTotal)

        val siloActualizado = silo.copy(
            cantidadDisponible = nuevaCantidad,
            porcentajeLlenado = nuevoPorcentaje,
            estado = nuevoEstado
        )
        siloDao.updateSilo(siloActualizado)
        return true
    }

    // Función de negocio según umbrales del caso
    private fun calcularEstado(cantidad: Double, capacidadTotal: Double): String {
        if (capacidadTotal <= 0) return "Desconocido"
        val porcentaje = (cantidad / capacidadTotal) * 100.0
        return when {
            porcentaje > 30.0 -> "Normal"
            porcentaje in 20.0..30.0 -> "Advertencia"
            else -> "Crítico"
        }
    }

    suspend fun insertarSilosIniciales() {
        val silosIniciales = listOf(
            SiloEntity(
                id = 1,
                granjaId = 101,
                tipoAlimento = "Maíz",
                capacidadTotal = 10000.0,
                cantidadDisponible = 8500.0,
                porcentajeLlenado = 85.0,
                estado = "Normal"
            ),
            SiloEntity(
                id = 2,
                granjaId = 102,
                tipoAlimento = "Trigo",
                capacidadTotal = 15000.0,
                cantidadDisponible = 4000.0,
                porcentajeLlenado = 26.0,
                estado = "Advertencia"
            ),
            SiloEntity(
                id = 3,
                granjaId = 103,
                tipoAlimento = "Soya",
                capacidadTotal = 12000.0,
                cantidadDisponible = 1200.0,
                porcentajeLlenado = 10.0,
                estado = "Crítico"
            )
        )
        siloDao.insertSilos(silosIniciales)
    }
}