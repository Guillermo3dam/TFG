package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.viewmodels.DogState
import com.example.tfg.models.viewmodels.DogViewModel
import com.example.tfg.navigation.AppScreens
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    date: String,
    navController: NavController,
    viewModel: DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Log.d("addreminderscreen", "Fecha que llega: $date")

    var dog by remember { mutableStateOf<Dog?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getDogs()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(Color.White),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    title = {
                        Text(text = "Acontecimiento nuevo", color = Color.Black)
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
            snackbarHost = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SnackbarHost(hostState = snackbarHostState)
                }
            },
            content = {
                Column(modifier = Modifier.padding(it)) { // Agregamos un Column en lugar de un Spacer
                    var selectedDogId by remember { mutableStateOf<String?>(null) }
                    val description = rememberSaveable { mutableStateOf("") }

                    when (val result = viewModel.state.value) {
                        is DogState.Success -> {
                            Column (
                                modifier = Modifier.padding(15.dp).fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Text(
                                        text = "Selecciona un perro",
                                        color = Color.Black
                                    )

                                LazyRow(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(result.data) { dog ->
                                        Spacer(modifier = Modifier.width(1.dp))

                                        ItemDog(
                                            dog = dog,
                                            isSelected = dog.id == selectedDogId,
                                            onClick = {
                                                selectedDogId =
                                                    if (selectedDogId == dog.id) null else dog.id
                                            },
                                            widthModifier = Modifier.width(200.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                }

                                Text(
                                    text = "Fecha seleccionada",
                                    color = Color.Black
                                )

                                DateText(date, true)


                                Text(
                                    text = "Denominación del acontecimiento",
                                    color = Color.Black
                                )

                                DogInputField1(
                                    valueState = description,
                                    labelId = "Añade una descripción",
                                    keyboardType = KeyboardType.Text
                                )
                            }
                        }
                        is DogState.Failure -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 80.dp)
                                    .background(Color.White)
                            ) {
                                Log.d("addreminderscreen", "Fail")
                                Text(text = "result.message")
                            }
                        }
                        is DogState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 80.dp)
                                    .background(Color.White)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Log.d("addreminderscreen", "Loading")
                                    Text(text = "Cargando datos..")
                                }
                            }
                        }
                        is DogState.Empty -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 80.dp)
                                    .background(Color.White)
                            ) {
                                Log.d("addreminderscreen", "empty")
                                Text(text = "No se han encontrado perros")
                            }
                        }
                    }
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateText(
    date: String,
    readOnly : Boolean
) {
    TextField(
        readOnly = readOnly,
        value = date,
        onValueChange = { /* lógica para actualizar el valor */ },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(241, 248, 247)
        )
    )
}