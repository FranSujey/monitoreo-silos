package com.example.monitoreo_silos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SiloDao {

    // Obtener todos los silos de manera reactiva
    @Query("SELECT * FROM silos ORDER BY id ASC")
    fun getAllSilos(): Flow<List<SiloEntity>>

    // Obtener silos pertenecientes a una granja específica
    @Query("SELECT * FROM silos WHERE granjaId = :granjaId")
    fun getSilosByGranja(granjaId: Int): Flow<List<SiloEntity>>

    // Obtener un silo específico por ID
    @Query("SELECT * FROM silos WHERE id = :id")
    suspend fun getSiloById(id: Int): SiloEntity?

    // Insertar un nuevo silo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSilo(silo: SiloEntity)

    // Métodos para inserción múltiple de silos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(silos: List<SiloEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSilos(silos: List<SiloEntity>)

    // Actualizar un silo existente
    @Update
    suspend fun updateSilo(silo: SiloEntity)

    // Actualizar nivel y estado directamente por Query
    @Query("UPDATE silos SET cantidadDisponible = :nuevaCantidad, estado = :nuevoEstado WHERE id = :siloId")
    suspend fun updateNivelYEstado(siloId: Int, nuevaCantidad: Double, nuevoEstado: String)
}