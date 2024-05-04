package com.example.tfg.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.navigation.AppScreens


@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        //Cabecera(Modifier.align(Alignment.TopEnd))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(35.dp))
            Imagen()
            Spacer(modifier = Modifier.padding(50.dp))
            CampoUsuario()
            Spacer(modifier = Modifier.padding(8.dp))
            CampoContraseña()
            Spacer(modifier = Modifier.padding(10.dp))
            TextoOlvidoContraseña()
            Spacer(modifier = Modifier.padding(8.dp))
            BotonLogin(navController)
            Spacer(modifier = Modifier.padding(2.dp))
            //DivisorOR()
            Spacer(modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.padding(65.dp))
            TextoRegistro()
        }
    }
}

@Composable
fun Cabecera(modificador: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = "cerrar app",
        modifier = modificador.clickable { activity.finish() })
}

@Composable
fun Imagen(){
    Row (verticalAlignment = Alignment.CenterVertically){
        Image(
            painter = painterResource(id = R.drawable.boxer),
            contentDescription = "logo",
            modifier = Modifier
                .size(200.dp, 85.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoUsuario() {
    var texto by rememberSaveable { mutableStateOf("") }
    TextField(
        value = texto,
        onValueChange = { texto = it },
        placeholder = { Text(text = "Correo electrónico", color = Color.Gray) },
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(247, 237, 237, 255)
        ),
        textStyle = TextStyle(color = Color.Black),
        modifier = Modifier
            .size(325.dp, 50.dp)
            .background(color = Color.White)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoContraseña() {
    var texto by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = texto,
        onValueChange = { texto = it },
        placeholder = { Text(text = "Contraseña", color = Color.Gray) },
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(247, 237, 237, 255)
        ),
        textStyle = TextStyle(color = Color.Black),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) {
                            R.drawable.ojovisible
                        } else {
                            R.drawable.ojoinvisible
                        }
                    ),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,

        modifier = Modifier
            .size(325.dp, 50.dp)
            .background(color = Color.Transparent)
    )
}

@Composable
fun BotonLogin(navController : NavController) {
    var enabled by remember { mutableStateOf(true) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)){
        Button(
            onClick = {
                      //enabled
                      navController.navigate(route = AppScreens.HomeScreen.route)

                      },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(2, 167, 36)
            ),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(16.dp)
                .clickable { true }
                .fillMaxWidth()

        ) {
            Text("Entrar")
        }
    }
}


@Composable
fun TextoOlvidoContraseña() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(end = 20.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "¿Has olvidado tu contraseña?",
            color = Color(2, 167, 36),
            modifier = Modifier
                .clickable {  }
        )
    }
}


@Composable
fun DivisorOR(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier
            .weight(1f)
            .height(0.7.dp)
        )
        Text(
            text = "OR",
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 30.dp)
        )
        Divider(modifier = Modifier
            .weight(1f)
            .height(0.7.dp)
        )
    }
}

@Composable
fun TextoRegistro() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
    Row(modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "¿No tienes cuenta?",
            color = Color.Gray
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Regístrate ahora.",
            color = Color(2, 167, 36),
            modifier = Modifier
                .clickable {

                }
        )
    }
}