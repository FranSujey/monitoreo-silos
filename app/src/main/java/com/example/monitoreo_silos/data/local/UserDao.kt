package com.example.monitoreo_silos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios WHERE username = :username AND passwordHash = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?
}