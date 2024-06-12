package com.jay.passwordmanager.domain

import androidx.lifecycle.LiveData
import com.jay.passwordmanager.datamodel.User
import com.jay.passwordmanager.database.UserDAO

class UserRepository(private val userDao: UserDAO) {

    fun getAllUser(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun insertUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}