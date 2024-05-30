package com.example.tfg.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.classes.Reminder
import com.example.tfg.models.viewmodels.DogState
import com.example.tfg.models.viewmodels.DogViewModel
import com.example.tfg.models.viewmodels.ReminderViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    date: String,
    navController: NavController,
    dogViewModel: DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    reminderViewModel: ReminderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Log.d("addreminderscreen", "Fecha que llega: $date")

    var dog by remember { mutableStateOf<Dog?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    var finalTime by remember { mutableStateOf<String?>(null) }
    val state = rememberTimePickerState(is24Hour = true)
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        dogViewModel.getDogs()
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
                Column(modifier = Modifier.padding(it)) {
                    var selectedDogId by remember { mutableStateOf<String?>(null) }
                    val description = rememberSaveable { mutableStateOf("") }

                    when (val result = dogViewModel.state.value) {
                        is DogState.Success -> {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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
                                                selectedDogId = if (selectedDogId == dog.id) null else dog.id
                                            },
                                            widthModifier = Modifier.width(200.dp),
                                            showDeleteIcon = false
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
                                    text = "Seleciona la hora",
                                    color = Color.Black
                                )



                                if (showDialog) {
                                    TimePickerDialog(
                                        onCancel = { showDialog = false },
                                        onConfirm = {
                                            val cal = java.util.Calendar.getInstance()
                                            cal.set(java.util.Calendar.HOUR_OF_DAY, state.hour)
                                            cal.set(java.util.Calendar.MINUTE, state.minute)
                                            cal.isLenient = false
                                            finalTime = formatter.format(cal.time)
                                            snackScope.launch {
                                                snackState.showSnackbar("Entered time: ${finalTime}")
                                            }
                                            showDialog = false
                                        },
                                    ) {
                                        TimeInput(state = state,
                                            colors = TimePickerDefaults.colors(
                                                timeSelectorSelectedContainerColor = Color(241, 248, 247),
                                                timeSelectorUnselectedContainerColor = Color(241, 248, 247)

                                            )
                                        )
                                    }
                                }


                                OutlinedButton(
                                    onClick = { showDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(55.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color(241, 248, 247),
                                        contentColor = Color.Black
                                    ),
                                    border = BorderStroke(1.dp, Color.Transparent),
                                    shape = RectangleShape
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        (if (finalTime != null) {
                                            finalTime
                                        } else {
                                            "Selecciona la hora"
                                        })?.let { it1 ->
                                            Text(
                                                text = it1,
                                                color = Color.Black,
                                                textAlign = TextAlign.Left,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "Denominación del acontecimiento",
                                    color = Color.Black
                                )

                                DogInputField1(
                                    valueState = description,
                                    labelId = "Añade una descripción",
                                    keyboardType = KeyboardType.Text
                                )

                                val reminder = selectedDogId?.let {
                                    finalTime?.let { it1 ->
                                        Reminder(
                                            dogId = it,
                                            description = description.value,
                                            date = date,
                                            hour = it1
                                        )
                                    }
                                }

                                SubmitButton(
                                    textId = "Añadir recordatorio",
                                    inputValido = description.value.isNotBlank() && selectedDogId != null && finalTime?.isNotBlank() == true
                                ) {
                                    if (reminder != null) {
                                        reminderViewModel.addReminderToCurrentUser(reminder)
                                        description.value = ""
                                        selectedDogId = null
                                        finalTime = null
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Has añadido un recordatorio")
                                        }
                                    }
                                }
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
                                Text(text = result.message)
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
    readOnly: Boolean
) {
    TextField(
        readOnly = readOnly,
        value = date,
        onValueChange = {  },
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


@Composable
fun TimePickerDialog(
    title: String = "Selecciona la hora",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = Color.White
                ),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(onClick = onCancel,
                        border = BorderStroke(1.dp, Color.Transparent)
                    ) {
                        Text("Cancelar", color = Color.Black)
                    }
                    Button(onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF57B262),
                            disabledContainerColor = Color(0xFF57B262))
                    ) {
                        Text("OK", color = Color.Black)
                    }
                }
            }
        }
    }
}
