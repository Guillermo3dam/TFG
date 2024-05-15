package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tfg.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(navController: NavController) {
     SettingsScreen(navController)
}

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    var showDialogExit by remember { mutableStateOf(false ) }
    var showDialogDeleteAccount by remember { mutableStateOf(false ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.07f))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsElevatedCard(
                option = "Editar perfil",
                icon = Icons.Outlined.AccountCircle
            ) {
                // añadir ruta a nueva ventana
            }

            SettingsElevatedCard(
                option = "Cambiar contraseña",
                icon = Icons.Outlined.Password
            ) {
                navController.navigate(route = AppScreens.UpdatePasswordScreen.route)
            }

            SettingsElevatedCard(
                option = "Eliminar cuenta",
                icon = Icons.Outlined.Delete
            ) {
                showDialogDeleteAccount = true
            }
            if (showDialogDeleteAccount) {
                ExitDialog(
                    onDismiss =  {showDialogDeleteAccount = false} ,
                    onConfirm = {
                        val user = Firebase.auth.currentUser!!
                        user.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User account deleted.")
                                    navController.navigate(route = AppScreens.LoginScreen.route)
                                }
                            }
                                },
                    title = "Borrar cuenta",
                    option = "¡¡ATENCION!! Tus datos se borrarán de forma PERMANENTE. ¿Seguro que quieres borrar tus datos?",
                    color = Color.Red
                )
            }
            SettingsElevatedCard(
                option = "Salir",
                icon = Icons.AutoMirrored.Outlined.Logout
            ) {
                showDialogExit = true
            }
            if (showDialogExit) {
                ExitDialog(
                    onDismiss =  {showDialogExit = false} ,
                    onConfirm = {
                        navController.navigate(route = AppScreens.LoginScreen.route)
                        FirebaseAuth.getInstance().signOut()
                    },
                    title = "Salir",
                    option = "¿Seguro que quieres salir?",
                    color = Color(0xFF57B262)
                )
            }
        }
    }
}

@Composable
fun ExitDialog(
    title : String,
    option: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    color: Color
){
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(text = "SI",
                    color =  color,
                    fontSize = 15.sp)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = "NO",
                    color = Color.Black,
                    fontSize = 15.sp)
            }
        },
        title = {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = title,
                    color = Color.Black)
            }
        },
        text = {
            Text(text = option,
                color = Color.Black)
        },
        containerColor = Color.White
    )
}

@Composable
fun SettingsElevatedCard(option: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color(241, 248, 247)
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, bottom = 7.dp, end = 15.dp)
            .height(60.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (option == "Eliminar cuenta")
                    Color.Red
                else
                    Color(0xFF57B262),
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = option,
                color = Color.Black
            )
        }
    }
}