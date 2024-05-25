package com.example.tfg.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.tfg.models.viewmodels.DogViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedDogScreen(
    id : String,
    navController: NavController,
    viewModel: DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }


    var dog by remember { mutableStateOf<Dog?>(null) }
    LaunchedEffect(id) {
        viewModel.getDogById(id) { foundDog ->
            dog = foundDog
        }
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
                        Text(text = "Datos de tu perro", color = Color.Black)
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
            }
        ) {
            if (dog != null) {
                Log.d("SelectedDogScreen", "Displaying details for dog: ${dog?.name}")
                MyDogForm(
                    Modifier.padding(it),
                    snackbarHostState = snackbarHostState,
                    navController = navController,
                    dog = dog!!
                )
            } else {
                Log.d("SelectedDogScreen", "No dog selected or loading...")
                Text(text = "Cargando detalles del perro...")
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDogForm(
    modifier: Modifier = Modifier,
    viewModel: DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    dog: Dog
) {
    val name = rememberSaveable { mutableStateOf(dog.name) }
    val state = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }

    var isMale by rememberSaveable { mutableStateOf(dog.male) }
    var isNeutered by rememberSaveable { mutableStateOf(dog.castrated) }
    var isPPP by rememberSaveable { mutableStateOf(dog.ppp) }

    var birthdate by rememberSaveable { mutableStateOf(formatDate(dog.birthday)) }
    var dateString = ""

    var allOptionsSelected by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "¿Cómo se llama?*",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DogInputField1(valueState = name, labelId = "Nombre del perro", keyboardType = KeyboardType.Text)

        Text(
            text = "¿Cuál es su fecha de nacimiento?*",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Log.d(ContentValues.TAG, "fecha string $birthdate")

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
                Text(
                    text = birthdate.ifEmpty { "Seleccionar la fecha" },
                    color = Color.Black,
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Text(
            text = "¿Es macho o hembra?*",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(true to "Macho", false to "Hembra").forEach { (value, label) ->
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (isMale == value),
                        onClick = { isMale = value },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = Color.Black,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }

        Text(
            text = "¿Está castrado?*",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(true to "Sí", false to "No").forEach { (value, label) ->
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (isNeutered == value),
                        onClick = { isNeutered = value },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = Color.Black,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isPPP,
                onCheckedChange = { isPPP = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Black
                )
            )
            Text(
                "¿Es Potencialmente Peligroso?",
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        allOptionsSelected = name.value.isNotBlank() && birthdate.isNotBlank()

        SubmitButton(textId = "Actualizar perro", inputValido = allOptionsSelected) {
            dateString = birthdate
            Log.d(ContentValues.TAG, "fecha string $dateString")

            viewModel.updateDog(
                dog.id,
                name = name.value,
                birthday = dateString,
                isMale = isMale,
                isNeutered = isNeutered,
                isPPP = isPPP
            )

            scope.launch {
                snackbarHostState.showSnackbar(
                    "Perro actualizado",
                    duration = SnackbarDuration.Short
                )
            }
        }

        if (showDialog) {
            DatePickerDialog(
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                ),
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF57B262),
                            disabledContainerColor = Color(0xFF57B262)
                        ),
                        onClick = {
                            birthdate = state.selectedDateMillis?.let {
                                formatDate(Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .toString())
                            }.orEmpty()
                            showDialog = false
                        },
                        border = BorderStroke(1.dp, Color.Transparent)
                    ) {
                        Text(text = "Confirmar", color = Color.Black)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDialog = false },
                        border = BorderStroke(1.dp, Color.Transparent)
                    ) {
                        Text(text = "Cancelar", color = Color.Black)
                    }
                }
            ) {
                MyDatePicker(state)
            }
        }
    }
}

fun formatDate(dateString: String): String {
    val parts = dateString.split("-")
    return if (parts.size == 3) {
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } else {
        dateString
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogInputField1(
    valueState: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true
) {
    TextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        placeholder = {
            Text(
                text = labelId,
                color = Color.Black,
                modifier = Modifier.padding(start = 6.dp)
            )
        },
        singleLine = isSingleLine,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            containerColor = Color(241, 248, 247)
        )
    )
}