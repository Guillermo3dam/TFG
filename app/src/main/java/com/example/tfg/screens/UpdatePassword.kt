package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePasswordScreen(navController: NavHostController) {
    val newPassword = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    val newPasswordVisible = rememberSaveable { mutableStateOf(false) }
    val confirmPasswordVisible = rememberSaveable { mutableStateOf(false) }

    var showNoPasswordChangedAlert by remember { mutableStateOf(false) }

    var showErrorPasswordMismatch by remember { mutableStateOf(false) }

    var showErrorPasswordLength by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    // Validar si las contraseñas coinciden
    val passwordsMatch = newPassword.value == confirmPassword.value

    // Validar si la contraseña tiene al menos 6 caracteres
    val validPasswordLength = newPassword.value.length >= 6
    val validConfirmPasswordLength = confirmPassword.value.length >= 6


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(Color.White),
                    colors = TopAppBarColors(
                        Color.White,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    ),
                    title = {
                        Text(text = "Cambio de contraseña", color = Color.Black)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(35.dp))

                    PasswordInput(
                        passwordState = newPassword,
                        labelId = "Introduce nueva contraseña",
                        passwordVisible = newPasswordVisible,
                        supportingText = if (newPassword.value.isNotEmpty() && !validPasswordLength) "La contraseña debe tener al menos 6 caracteres" else if (!passwordsMatch) "Las contraseñas no coinciden" else null
                    )
                    PasswordInput(
                        passwordState = confirmPassword,
                        labelId = "Confirmar nueva contraseña",
                        passwordVisible = confirmPasswordVisible,
                        supportingText = if (newPassword.value.isNotEmpty() && !validConfirmPasswordLength) "La contraseña debe tener al menos 6 caracteres" else if (!passwordsMatch) "Las contraseñas no coinciden" else null
                    )

                    SubmitButton(
                        textId = "Confirmar",
                        inputValido = passwordsMatch && validPasswordLength
                    ) {
                        val user = Firebase.auth.currentUser
                        val newPasswordText = newPassword.value
                        val confirmPasswordText = confirmPassword.value

                        // Verificar si se han ingresado contraseñas
                        if (newPasswordText.isNotEmpty() && confirmPasswordText.isNotEmpty()) {
                            // Verificar si las contraseñas coinciden
                            if (newPasswordText == confirmPasswordText) {
                                // Verificar si la contraseña tiene al menos 6 caracteres
                                if (validPasswordLength) {
                                    // Actualizar la contraseña del usuario
                                    user?.updatePassword(newPasswordText)
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Mostrar mensaje de contraseña actualizada con éxito
                                                showNoPasswordChangedAlert = true
                                                Log.d(TAG, "User password updated.")
                                            } else {
                                                // Mostrar mensaje de error si no se pudo actualizar la contraseña
                                                showErrorPasswordMismatch = true
                                                Log.e(TAG, "Failed to update user password.", task.exception)
                                            }
                                        }
                                    // Reiniciar el estado de mostrar error
                                    showErrorPasswordMismatch = false
                                    newPassword.value = ""
                                    confirmPassword.value = ""

                                    // Ocultar el teclado después de pulsar el botón
                                    keyboardController?.hide()
                                } else {
                                    // Mostrar mensaje de error si la contraseña no tiene al menos 6 caracteres
                                    showErrorPasswordLength = true
                                }
                            } else {
                                // Mostrar mensaje de contraseñas no coincidentes
                                showErrorPasswordMismatch = true
                            }
                        }
                    }

                    // Mostrar el mensaje de contraseña no cambiada si showNoPasswordChangedAlert es true
                    if (showNoPasswordChangedAlert) {
                        ShowPasswordChangedDialog(onDismiss = { showNoPasswordChangedAlert = false })
                    }

                    // Mostrar el mensaje de error si las contraseñas no coinciden
                    if (showErrorPasswordMismatch) {
                        ShowErrorDialog(onDismiss = { showErrorPasswordMismatch = false }, title = "Error", message = "Las contraseñas no coinciden")
                    }

                    // Mostrar el mensaje de error si la contraseña no tiene al menos 6 caracteres
                    if (showErrorPasswordLength) {
                        ShowErrorDialog(onDismiss = { showErrorPasswordLength = false }, title = "Error", message = "La contraseña debe tener al menos 6 caracteres")
                    }
                }
            }
        )
    }
}


@Composable
fun ShowErrorDialog(
    onDismiss: () -> Unit,
    title: String,
    message: String
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("OK")
            }
        },
        title = { Text(title) },
        text = { Text(message) }
    )
}

@Composable
fun ShowPasswordChangedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("OK")
            }
        },
        title = { Text("Contraseña actualizada") }
    )
}


