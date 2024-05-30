package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.models.viewmodels.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController, viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val email = rememberSaveable { mutableStateOf("") }
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()

    val valido = remember(email.value) { email.value.trim().isNotEmpty() && isEmailValid}

    val keyboardController = LocalSoftwareKeyboardController.current

    var dialogVisible by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(Color.White),
                    colors = TopAppBarColors(
                        containerColor = Color.White,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    ),
                    title = {
                        Text(text = "Restablece tu contraseña", color = Color.Black)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido a Mi Pantalla",
                        color = Color.Black,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Labels
                    EmailInput(
                        emailState = email,
                        showErrorEmailFormat = isEmailValid
                    )

                    SubmitButton(textId = "Confirmar", inputValido = valido) {
                        viewModel.sendPasswordResetEmail(
                            email = email.value,
                            onSuccess = {
                                dialogTitle = "Éxito"
                                dialogMessage = "Se ha enviado un correo electrónico para restablecer tu contraseña."
                                dialogVisible = true
                            },
                            onError = {
                                dialogTitle = "Error"
                                dialogMessage = "El correo no se encuentra registrado."
                                dialogVisible = true
                            }
                        )
                        keyboardController?.hide()
                        }
                    }

                    // Diálogo para mostrar mensajes
                    if (dialogVisible) {
                        AlertDialog(
                            containerColor = Color.White,
                            onDismissRequest = { dialogVisible = false },
                            title = { Text(dialogTitle) },
                            text = { Text(dialogMessage) },
                            confirmButton = {
                                Button(
                                    onClick = { dialogVisible = false },
                                ) {
                                    Text("OK")
                                }
                            }
                        )
                    }

            }
        )
    }
}