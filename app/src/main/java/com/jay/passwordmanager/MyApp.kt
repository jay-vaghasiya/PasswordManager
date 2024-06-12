package com.jay.passwordmanager

import android.app.Application
import androidx.room.Room
import com.jay.passwordmanager.database.AppDatabase
import com.jay.passwordmanager.domain.UserRepository

class MyApp : Application() {

    companion object {
        const val DATABASE_NAME = "password_manager_database"
        lateinit var database: AppDatabase
            private set

        lateinit var userRepository: UserRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).build()

        userRepository = UserRepository(database.userDao())

    }
}