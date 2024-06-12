package com.jay.passwordmanager.presentation

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.jay.passwordmanager.BioMetricPromptManager
import com.jay.passwordmanager.domain.UserViewModel
import com.jay.passwordmanager.presentation.componets.BottomSheetContent
import com.jay.passwordmanager.presentation.componets.ListItem
import com.jay.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private val promptManager by lazy {
        BioMetricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setContent {
            PasswordManagerTheme {
                PasswordManagerUI()
                val biometricResult by promptManager.promptResult.collectAsState(
                    initial = null
                )
                val enrollLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
                        onResult = { println("Activity Result : $it") })
                LaunchedEffect(biometricResult) {
                    if (biometricResult is BioMetricPromptManager.BiometricResult.AuthenticationNotSet) {
                        if (Build.VERSION.SDK_INT >= 30) {
                            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                putExtra(
                                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                                )
                            }
                            enrollLauncher.launch(enrollIntent)
                        }
                    }

                }

                LaunchedEffect(Unit) {
                    promptManager.showBiometricPrompt(
                        title = "Biometric",
                        description = "Security"
                    )
                }

                biometricResult?.let { result ->
                        when (result) {
                            is BioMetricPromptManager.BiometricResult.AuthenticationError -> result.error
                            BioMetricPromptManager.BiometricResult.AuthenticationFailed -> {
                               showToast ("Authentication failed")
                            }

                            BioMetricPromptManager.BiometricResult.AuthenticationNotSet -> {
                                showToast("Authentication not set")
                            }

                            BioMetricPromptManager.BiometricResult.AuthenticationSuccess -> {
                                showToast("Authentication success")
                            }

                            BioMetricPromptManager.BiometricResult.FeatureUnavailable -> {
                                showToast("Feature unavailable")
                            }

                            BioMetricPromptManager.BiometricResult.HardwareUnavailable -> {
                                showToast("Hardware unavailable")
                            }
                        }


                }
            }
        }
    }

    fun showToast(message: String){
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordManagerUI() {
        val bottomSheetState = rememberModalBottomSheetState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Password Manager") },
//                    Modifier.background(color = MaterialTheme.colorScheme.background)
//                )
//            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier,
                    onClick = {
                        coroutineScope.launch {
                            if (bottomSheetState.isVisible) {
                                bottomSheetState.hide()
                            } else {
                                bottomSheetState.show()
                            }
                        }
                    },
                    containerColor = Color.Blue,
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add",
                            tint = Color.White

                        )
                    })

            }
        ) { padding ->
            CardItem(padding)
            if (bottomSheetState.isVisible) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },
                    content = { BottomSheetContent(viewModel = viewModel) },
                    onDismissRequest = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun CardItem(padding: PaddingValues) {
        val notes by viewModel.getAllNotes().observeAsState(initial = emptyList())
        viewModel.getAllNotes()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            items(
                notes
            ) { item ->
                ListItem(item, viewModel)
            }
        }

    }


    @Preview
    @Composable
    fun DefaultPreview() {
        PasswordManagerUI()

    }
}

