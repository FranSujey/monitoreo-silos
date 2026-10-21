package com.example.monitoreo_silos.data.repository



import com.example.monitoreo_silos.data.local.SiloDao
import com.example.monitoreo_silos.data.local.SiloEntity
import kotlinx.coroutines.flow.Flow

class SiloRepository(private val siloDao: SiloDao) {

    // Flujo continuo de la lista de silos para observar en la UI
    val allSilos: Flow<List<SiloEntity>> = siloDao.getAllSilos()

    suspend fun insertSilo(silo: SiloEntity) {
        siloDao.insertSilo(silo)
    }

    suspend fun cargarDatosPrueba(silosFicticios: List<SiloEntity>) {
        siloDao.insertAll(silosFicticios)
    }

    // Registrar Carga de Alimento (Suma stock)
    suspend fun registrarCarga(siloId: Int, cantidadIngresada: Double): Boolean {
        val silo = siloDao.getSiloById(siloId) ?: return false
        val nuevaCantidad = (silo.cantidadDisponible + cantidadIngresada).coerceAtMost(silo.capacidadTotal)
        val nuevoEstado = calcularEstado(nuevaCantidad, silo.capacidadTotal)

        val siloActualizado = silo.copy(
            cantidadDisponible = nuevaCantidad,
            estado = nuevoEstado
        )
        siloDao.updateSilo(siloActualizado)
        return true
    }

    // Registrar Descarga/Consumo de Alimento (Resta stock)
    suspend fun registrarDescarga(siloId: Int, cantidadRetirada: Double): Boolean {
        val silo = siloDao.getSiloById(siloId) ?: return false
        val nuevaCantidad = (silo.cantidadDisponible - cantidadRetirada).coerceAtLeast(0.0)
        val nuevoEstado = calcularEstado(nuevaCantidad, silo.capacidadTotal)

        val siloActualizado = silo.copy(
            cantidadDisponible = nuevaCantidad,
            estado = nuevoEstado
        )
        siloDao.updateSilo(siloActualizado)
        return true
    }

    // Función auxiliar de negocio según los umbrales del caso
    private fun calcularEstado(cantidad: Double, capacidadTotal: Double): String {
        if (capacidadTotal <= 0) return "Desconocido"
        val porcentaje = (cantidad / capacidadTotal) * 100.0
        return when {
            porcentaje > 30.0 -> "Normal"
            porcentaje in 20.0..30.0 -> "Advertencia"
            else -> "Crítico"
        }
    }
}
