package com.example.tfg

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tfg.navigation.AppNavigation
import com.example.tfg.navigation.BottomNavigation
import com.example.tfg.ui.theme.TFGTheme
import com.example.tfg.navigation.AppScreens.LoginScreen
import com.example.tfg.navigation.currentRoute


class MainActivity : ComponentActivity() {
    //private val dbHelper = DatabaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this)

        setContent {
            TFGTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                if (!currentRoute(navController).equals(LoginScreen.route))
                    BottomNavigation(navController)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AppNavigation(navController)
            }
        }
}



@Composable
fun UserInputField(hint: String) {
    // Aquí implementa el campo de entrada de texto para el usuario y contraseña
    // Puedes usar la API de composición de Jetpack para crear los campos de entrada
    // como TextField y PasswordField
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    //AppNavigation()
}

