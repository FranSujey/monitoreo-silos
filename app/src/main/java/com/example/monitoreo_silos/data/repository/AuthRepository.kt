package com.example.monitoreo_silos.repository

import com.example.monitoreo_silos.data.local.UserDao
import com.example.monitoreo_silos.data.local.UserEntity

class AuthRepository(private val userDao: UserDao) {

    suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    suspend fun register(user: UserEntity): Boolean {
        val existing = userDao.getUserByUsername(user.username)
        if (existing != null) return false // El usuario ya existe
        userDao.insertUser(user)
        return true
    }
}