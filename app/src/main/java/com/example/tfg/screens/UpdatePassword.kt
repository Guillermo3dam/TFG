package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePasswordScreen(navController: NavHostController) {

    // Variables de estado para la nueva contraseña, confirmación y visibilidad de los campos de contraseña
    val newPassword = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    val newPasswordVisible = rememberSaveable { mutableStateOf(false) }
    val confirmPasswordVisible = rememberSaveable { mutableStateOf(false) }

    // Estado para mostrar el mensaje de contraseña no cambiada
    var showNoPasswordChangedAlert by remember { mutableStateOf(false) }

    // Estado para mostrar el mensaje de error si las contraseñas no coinciden
    var showError by remember { mutableStateOf(false) }

    // Controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current

    val valido = remember(confirmPassword.value, newPassword.value) {
        confirmPassword.value.trim().isNotEmpty() && newPassword.value.trim().isNotEmpty() && newPassword.value == confirmPassword.value
    }
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
                    Text(
                        text = "Bienvenido a Mi Pantalla",
                        color = Color.Black,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Labels y campos de entrada de contraseña
                    PasswordInput(passwordState = newPassword, labelId = "Introduce nueva contraseña", passwordVisible = newPasswordVisible)
                    PasswordInput(passwordState = confirmPassword, labelId = "Confirmar nueva contraseña", passwordVisible = confirmPasswordVisible)

                    // Botón de confirmación
                    SubmitButton(
                        textId = "Confirmar",
                        inputValido = valido
                    ) {

                        val user = Firebase.auth.currentUser
                        val newPasswordText = newPassword.value
                        val confirmPasswordText = confirmPassword.value

                        // Verificar si se han ingresado contraseñas
                        if (newPasswordText.isNotEmpty() && confirmPasswordText.isNotEmpty()) {
                            // Verificar si las contraseñas coinciden
                            if (newPasswordText == confirmPasswordText) {
                                // Actualizar la contraseña del usuario
                                user?.updatePassword(newPasswordText)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Mostrar mensaje de contraseña actualizada con éxito
                                            showNoPasswordChangedAlert = true
                                            Log.d(TAG, "User password updated.")
                                        } else {
                                            // Mostrar mensaje de error si no se pudo actualizar la contraseña
                                            showError = true
                                            Log.e(TAG, "Failed to update user password.", task.exception)
                                        }
                                    }
                                // Reiniciar el estado de mostrar error
                                showError = false
                                newPassword.value = ""
                                confirmPassword.value = ""

                                // Ocultar el teclado después de pulsar el botón
                                keyboardController?.hide()
                            } else {
                                // Mostrar mensaje de contraseñas no coincidentes
                                showError = true
                            }
                        } else {
                            // Mostrar mensaje de alerta si alguna de las contraseñas está vacía
                            showError = true
                        }
                    }

                    // Mostrar el mensaje de contraseña no cambiada si showNoPasswordChangedAlert es true
                    if (showNoPasswordChangedAlert) {
                        showPasswordChangedDialog(onDismiss = { showNoPasswordChangedAlert = false })
                    }

                    // Mostrar el mensaje de error si las contraseñas no coinciden
                    if (showError) {
                        showErrorDialog(onDismiss = { showError = false })
                    }
                }
            }
        )
    }
}

@Composable
fun showPasswordChangedDialog(
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

@Composable
fun showErrorDialog(
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
        title = { Text("Error") },
        text = { Text("Las contraseñas deben tener al menos 6 caracteres") }
    )
}
