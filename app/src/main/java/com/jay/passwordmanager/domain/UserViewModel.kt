package com.jay.passwordmanager.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jay.passwordmanager.MyApp
import com.jay.passwordmanager.datamodel.User
import kotlinx.coroutines.launch

class UserViewModel() : ViewModel() {

    fun getAllNotes(): LiveData<List<User>> {
        return MyApp.userRepository.getAllUser()
    }

    fun insertNotes(user: User) {
        viewModelScope.launch {
            MyApp.userRepository.insertUser(user)
        }
    }


    fun updateUser(user: User) {
        viewModelScope.launch {
            MyApp.userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            MyApp.userRepository.deleteUser(user)
        }
    }

}