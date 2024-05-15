package com.example.tfg.screens

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.navigation.AppScreens
import com.example.tfg.models.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun LoginScreen(navController: NavController) {

    nuevaLogin(navController)
/*
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
    }*/
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
fun Logo(){
        Image(
            painter = painterResource(id = R.drawable.boxer),
            contentDescription = "logo",
            modifier = Modifier
                .size(200.dp, 85.dp)
        )
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
                containerColor = Color(0xFF57B262)
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
fun ForgorPassword(navController : NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 15.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "¿Has olvidado tu contraseña?",
            color = Color(0xFF57B262),
            modifier = Modifier
                .clickable {
                    navController.navigate(route = AppScreens.ForgotPasswordScreen.route)
                }
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
fun SignUp() {
    HorizontalDivider(
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
            color =  Color(0xFF57B262),
            modifier = Modifier
                .clickable {

                }
        )
    }
}


@Composable
fun nuevaLogin(
    navController: NavController,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ){

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)){

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 50.dp)) {
            Logo()
            Spacer(modifier = Modifier.padding(top = 70.dp ))
            if(showLoginForm.value){
                Text(text = "Inicia sesion",
                    color = Color.Black)
                UserForm(
                    isCreateAccount = false,
                    navController = navController
                ){
                    email, password ->
                    Log.d("BestFriend", "Logueando con $email y $password")
                    viewModel.signInWithEmailAndPassword(email, password){
                        navController.navigate(route = AppScreens.HomeScreen.route)
                    }
                }
            }
            else{
                Text(text = "Crea una cuenta",
                    color = Color.Black)
                UserForm(
                    isCreateAccount = true,
                    navController = navController
                )
                {
                    email, password ->
                    Log.d("BestFriend", "Creando cuenta con $email y $password")
                    viewModel.createUsersWithEmailAndPassword(email, password){
                        navController.navigate(route = AppScreens.HomeScreen.route)

                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 158.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            Spacer(modifier = Modifier.padding(top = 14.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                val text1 =
                    if(showLoginForm.value) "¿No tienes cuenta?"
                else "¿Ya tienes cuenta?"
                val text2 =
                    if(showLoginForm.value) "¿Regístrate?"
                    else "¿Inicia sesión?"

                Text(text = text1, color = Color.Black)
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color =  Color(0xFF57B262),
                )

            }
        }
    }

}

@Composable
fun UserForm(
    navController : NavController,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = {email, pw ->}
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = remember (email.value, password.value){
        email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(
            emailState = email
        )
        PasswordInput(
            passwordState = password,
            labelId = "Contraseña",
            passwordVisible = passwordVisible
        )
        if(!isCreateAccount)
            ForgorPassword(navController)
        else
            Spacer(modifier = Modifier.padding(16.dp))



        Spacer(modifier = Modifier.padding(8.dp))
        SubmitButton(
            textId = if(isCreateAccount) "Registrarse" else "Entrar",
            inputValido = valido
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido : Boolean,
    onClick : () -> Unit
) {
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF57B262),
            disabledContainerColor = Color(0xFF57B262)
    ),
        shape = MaterialTheme.shapes.small,
        enabled = inputValido,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Text(text = textId,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if(passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId, color = Color.Black)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 15.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        visualTransformation =visualTransformation,
        trailingIcon = {
            if(passwordState.value.isNotBlank()){
                PasswordVisibleIcon(passwordVisible)
            }
            else null
        },
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(241, 248, 247)

        )
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image = if(passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = "",
            tint = Color.Black)
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId : String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine : Boolean = true
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = { Text(text = labelId, color = Color.Black)},
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 15.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(241, 248, 247)
        )
    )
}