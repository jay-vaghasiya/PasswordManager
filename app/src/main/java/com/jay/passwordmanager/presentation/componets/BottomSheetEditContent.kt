package com.jay.passwordmanager.presentation.componets

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
import com.jay.passwordmanager.datamodel.User
import com.jay.passwordmanager.domain.UserViewModel
import com.jay.passwordmanager.security.AesEncryption

@Composable
fun BottomSheetEditContent(user: User, viewModel: UserViewModel) {

    val key = AesEncryption.generateKey()

    var userName by remember {
        mutableStateOf(user.acName)
    }
    var email by remember {
        mutableStateOf(user.email)
    }
    var password by remember { mutableStateOf(user.password) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        userName?.let { name ->
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { userName = it },
                label = { Text("Account name") },
                placeholder = { Text("Enter account name") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        email?.let { mail ->
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = mail,
                onValueChange = { email = it },
                label = { Text("Username / Email") },
                placeholder = { Text("Enter username or email") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        password?.let { paas ->
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = paas,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter password") },
                visualTransformation = PasswordVisualTransformation()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                val encryptedPassword = AesEncryption.encrypt(password.toString(), key)
                val newUser =
                    User(id = user.id, acName = userName, email = email, password = encryptedPassword.toString())

                viewModel.updateUser(newUser)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)

        ) {
            Text(
                text = "SaveAccount",
                color = Color.White
            )
        }
    }
}