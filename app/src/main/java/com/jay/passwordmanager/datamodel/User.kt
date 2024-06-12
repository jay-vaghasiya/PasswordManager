package com.jay.passwordmanager.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var acName: String? = "",
    var email: String? = "",
    val password: String? = "",
    )
