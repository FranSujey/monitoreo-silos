package com.example.monitoreo_silos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val nombre: String,
    val rol: String // "OPERARIO", "SUPERVISOR", "JEFATURA"
)