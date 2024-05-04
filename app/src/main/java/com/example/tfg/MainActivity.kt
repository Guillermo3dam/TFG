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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.tfg.navigation.AppNavigation
import com.example.tfg.screens.LoginScreen
import com.example.tfg.ui.theme.TFGTheme


class MainActivity : ComponentActivity() {
    //private val dbHelper = DatabaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this)

        setContent {
            TFGTheme {
                // A surface container using the 'background' color from the theme
                var seleccionado by rememberSaveable {
                    mutableStateOf("")
                }
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavegation(navController)
        }
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
        ){
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

