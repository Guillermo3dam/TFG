package com.example.tfg.screens

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.navigation.AppScreens
import com.example.tfg.models.viewmodels.LoginViewModel


@Composable
fun LoginScreen(navController: NavController) {
    Login(navController)
}

@Composable
fun Logo(){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(175.dp)
        )
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
fun Login(
    navController: NavController,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(0.05f))
            Logo()
            Spacer(modifier = Modifier.weight(0.1f))
            if (showLoginForm.value) {
                Text(text = "Inicia sesión", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                UserForm(
                    isCreateAccount = false,
                    navController = navController
                ) { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password, {
                        navController.navigate(route = AppScreens.HomeScreen.route)
                    }) { error ->
                        errorMessage = error
                    }
                }
                if (errorMessage.isNotEmpty()) {
                    AlertDialog(
                        containerColor = Color.White,
                        onDismissRequest = { errorMessage = "" },
                        title = { Text(text = "Error") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(onClick = { errorMessage = "" }) {
                                Text("OK")
                            }
                        }
                    )
                }
            } else {
                Text(text = "Crea una cuenta", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                UserForm(
                    isCreateAccount = true,
                    navController = navController
                ) { email, password ->
                    Log.d("BestFriend", "Creando cuenta con $email y $password")
                    viewModel.createUsersWithEmailAndPassword(email, password, {
                        navController.navigate(route = AppScreens.HomeScreen.route)
                    }) { error ->
                        errorMessage = error
                    }
                }
                if (errorMessage.isNotEmpty()) {
                    AlertDialog(
                        containerColor = Color.White,
                        onDismissRequest = { errorMessage = "" },
                        title = { Text(text = "Error") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(onClick = { errorMessage = "" }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().height(1.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = if (showLoginForm.value) "¿No tienes cuenta?" else "¿Ya tienes cuenta?"
                val text2 = if (showLoginForm.value) "¿Regístrate?" else "¿Inicia sesión?"

                Text(text = text1, color = Color.Black)
                Text(
                    text = text2,
                    modifier = Modifier.clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = Color(0xFF57B262),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun UserForm(
    navController : NavController,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = {email, pw ->}
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }


    val validPasswordLength = password.value.length >= 6
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()

    val isValid = remember(email.value, password.value, isEmailValid, validPasswordLength) {
        email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty() &&
                isEmailValid &&
                validPasswordLength
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        EmailInput(
            emailState = email,
            showErrorEmailFormat = isEmailValid,
            )
        PasswordInput(
            passwordState = password,
            labelId = "Contraseña",
            passwordVisible = passwordVisible,
            supportingText = if (password.value.isNotEmpty() && !validPasswordLength) "La contraseña debe tener al menos 6 caracteres" else null

        )
        if(!isCreateAccount)
            ForgorPassword(navController)
        else
            Spacer(modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.padding(8.dp))
        SubmitButton(
            textId = if(isCreateAccount) "Registrarse" else "Entrar",
            inputValido = isValid
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
    passwordVisible: MutableState<Boolean>,
    supportingText: String? = null
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
        },
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(241, 248, 247)
        ),
        supportingText = {
            if (supportingText?.isNotEmpty() == true) {
                Text(text = supportingText, color = Color.Red)
            }
        }
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
    labelId : String = "Email",
    showErrorEmailFormat : Boolean = false,
    ) {

    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
        supportingText = if (emailState.value.isNotEmpty() && !showErrorEmailFormat) "El correo electrónico no está bien formado" else null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine : Boolean = true,
    supportingText: String? = null

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
        ),
        supportingText = {
            if (supportingText?.isNotEmpty() == true) {
                Text(text = supportingText, color = Color.Red)
            }
        }
    )
}