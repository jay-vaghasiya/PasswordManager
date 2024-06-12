package com.jay.passwordmanager.usecases

import com.jay.passwordmanager.Constants

class ValidateUserDataUseCase {

    fun isValidUserName(fullName: String): String {
        val trimmedFullName = fullName.trim()
        return if (trimmedFullName.isNotEmpty() && trimmedFullName.length >= 3) {
            Constants.VALID
        } else {
            Constants.USERNAME_VALIDATION_MSG
        }
    }

    fun isValidEmail(email: String): String {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Constants.VALID
        } else {
            Constants.INVALID_EMAIL_MSG
        }
    }

    fun isValidPassword(password: String): String {
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasValidLength = password.length > 8

        return if (hasUppercase && hasLowercase && hasDigit && hasValidLength) {
            Constants.VALID
        } else {
            Constants.PASSWORD_VALIDATION_MSG
        }
    }

}