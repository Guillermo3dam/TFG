package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tfg.dbutils.FirestoreManager
import com.example.tfg.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    navController: NavController
) {
     Settings(
         navController
     )
}

@Composable
fun Settings(
    navController: NavController,
    viewModel: FirestoreManager = androidx.lifecycle.viewmodel.compose.viewModel()
) {

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
            ItemSettings(
                option = "Editar perfil",
                icon = Icons.Outlined.AccountCircle
            ) {
                // añadir ruta a nueva ventana
            }

            ItemSettings(
                option = "Cambiar contraseña",
                icon = Icons.Outlined.Password
            ) {
                navController.navigate(route = AppScreens.UpdatePasswordScreen.route)
            }

            ItemSettings(
                option = "Eliminar cuenta",
                icon = Icons.Outlined.Delete
            ) {
                showDialogDeleteAccount = true
            }
            if (showDialogDeleteAccount) {
                MyDialog(
                    onDismiss = { showDialogDeleteAccount = false },
                    onConfirm = {
                        val user = Firebase.auth.currentUser
                        val auth: FirebaseAuth = Firebase.auth
                        val userEmail = auth.currentUser?.email

                        if (user != null && userEmail != null) {
                            // Delete the user document from Firestore first
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    viewModel.deleteUser(userEmail)
                                    Log.d(TAG, "User document deleted from Firestore.")

                                    // After the user document is deleted, delete the user from Firebase Auth
                                    user.delete()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d(TAG, "User account deleted.")
                                                navController.navigate(route = AppScreens.LoginScreen.route)
                                            } else {
                                                Log.w(TAG, "Failed to delete user account: ${task.exception}")
                                            }
                                        }
                                } catch (e: Exception) {
                                    Log.w(TAG, "Error deleting user document: ${e.message}")
                                }
                            }
                        } else {
                            Log.w(TAG, "No authenticated user found.")
                        }
                    },
                    title = "Borrar cuenta",
                    option = "¡¡ATENCION!! Tus datos se borrarán de forma PERMANENTE. ¿Seguro que quieres borrar tus datos?",
                    color = Color.Red
                )
            }
            ItemSettings(
                option = "Salir",
                icon = Icons.AutoMirrored.Outlined.Logout
            ) {
                showDialogExit = true
            }
            if (showDialogExit) {
                MyDialog(
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
fun MyDialog(
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
                Text(text = "OK",
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
                Text(text = "Cancelar",
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
fun ItemSettings(option: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = Color(241, 248, 247)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
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