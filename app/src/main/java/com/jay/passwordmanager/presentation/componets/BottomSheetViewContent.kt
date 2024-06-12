package com.jay.passwordmanager.presentation.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jay.passwordmanager.datamodel.User
import com.jay.passwordmanager.domain.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetViewContent(user: User, viewModel: UserViewModel) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Account Details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Account Type",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold

        )
        Text(
            text = user.acName.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold

        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Username / Email",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold

        )
        Text(
            text = user.email.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold

        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Password",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold

        )
        Text(
            text = "*************",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (bottomSheetState.isVisible) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },
                    content = { BottomSheetEditContent(user = user, viewModel = viewModel) },
                    onDismissRequest = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }
                )
            }
            Button(
                modifier = Modifier
                    .padding()
                    .weight(1f)
                    .padding(12.dp),
                onClick = { coroutineScope.launch {
                    if (bottomSheetState.isVisible) {
                        bottomSheetState.hide()
                    } else {
                        bottomSheetState.show()
                    }
                } },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),

                ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Edit",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                onClick = { viewModel.deleteUser(user) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Delete",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}