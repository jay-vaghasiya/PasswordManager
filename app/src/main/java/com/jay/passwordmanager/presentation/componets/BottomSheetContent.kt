package com.jay.passwordmanager.presentation.componets

import com.jay.passwordmanager.security.AesEncryption
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.jay.passwordmanager.Constants
import com.jay.passwordmanager.datamodel.User
import com.jay.passwordmanager.domain.UserViewModel
import com.jay.passwordmanager.usecases.ValidateUserDataUseCase

@Composable
fun BottomSheetContent(viewModel: UserViewModel) {
    var accountName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var iv by remember { mutableStateOf("") }
    val key = AesEncryption.generateKey()

    val validateUserDataUseCase = ValidateUserDataUseCase()

    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = accountName,
            onValueChange = {
                accountName = it
                usernameError = validateUserDataUseCase.isValidUserName(accountName)
            },
            label = { Text("Account name") },
            placeholder = { Text("Enter account name") },

            supportingText = {
                if (usernameError == Constants.VALID) {
                    Text(text = usernameError)
                }
            }

        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = {
                username = it
                emailError = validateUserDataUseCase.isValidEmail(it)
            },
            label = { Text("Username / Email") },
            placeholder = { Text("Enter username or email") },
            supportingText = {
                if (emailError == Constants.VALID) {
                    Text(text = emailError)
                }
            }

        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
                passwordError = validateUserDataUseCase.isValidPassword(password)
            },
            label = { Text("Password") },
            placeholder = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            supportingText = {
                if (passwordError == Constants.VALID) {
                    Text(text = passwordError)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (usernameError == Constants.VALID && emailError == Constants.VALID && passwordError == Constants.VALID) {
                    val encryptedPassword = AesEncryption.encrypt(password, key)
                    val user =
                        User(acName = accountName, email = username, password = encryptedPassword.toString())
                    viewModel.insertNotes(user)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)

        ) {
            Text(
                text = "Add New Account",
                color = Color.White
            )
        }
    }
}