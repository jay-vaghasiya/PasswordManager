package com.jay.passwordmanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jay.passwordmanager.datamodel.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
}