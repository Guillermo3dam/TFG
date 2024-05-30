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
import androidx.navigation.NavHostController
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.viewmodels.DogViewModel
import com.example.tfg.navigation.AppScreens
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
            AddDogForm(
                Modifier.padding(it),
                snackbarHostState = snackbarHostState,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogForm(
    modifier: Modifier = Modifier,
    viewModel : DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {

    val name = rememberSaveable { mutableStateOf("") }
    val state = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }
    val gender = rememberSaveable { mutableStateOf("") }

    var isNeutered by remember { mutableStateOf(false) }
    var isPPP by remember { mutableStateOf(false) }

    val genderOptions = listOf("Macho", "Hembra")
    val selectedGender = rememberSaveable { mutableStateOf(genderOptions[0]) }

    val neuteredOptions = listOf("Si", "No")
    val selectedNeutered = rememberSaveable { mutableStateOf(neuteredOptions[0]) }

    // Mutable state for the selected date
    var birthdate by remember { mutableStateOf<LocalDate?>(null) }
    var dateString = ""


    var allOptionsSelected by remember { mutableStateOf(false) }


    var isMale by remember { mutableStateOf<Boolean?>(null) }
    var isDogNeutered by remember { mutableStateOf<Boolean?>(null) }

    val scope = rememberCoroutineScope()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "¿Como se llama?*",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DogInputField(valueState = name, labelId = "Nombre del perro", keyboardType = KeyboardType.Text)

        Text(
            text = "¿Cual es su fecha de nacimiento?*",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(241, 248, 247),
                contentColor = Color.Black),
            border = BorderStroke(1.dp, Color.Transparent),
            shape = RectangleShape
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (birthdate != null) {
                        "${birthdate!!.dayOfMonth}/${birthdate!!.monthValue}/${birthdate!!.year}"
                    } else {
                        "Seleccionar la fecha"
                    },
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
                        onClick = {
                            isMale = value
                            selectedGender.value = label
                            gender.value = label
                        },
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
            listOf(true to "Si", false to "No").forEach { (value, label) ->
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (isDogNeutered == value),
                        onClick = {
                            isDogNeutered = value
                            selectedNeutered.value = label
                            isNeutered = value
                        },
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
        allOptionsSelected = selectedGender.value.isNotBlank() && selectedNeutered.value.isNotBlank() && name.value.isNotBlank() && birthdate != null && isMale != null && isDogNeutered != null

        SubmitButton(textId = "Añadir perro", inputValido = allOptionsSelected) {
            dateString = "${birthdate!!.dayOfMonth}/${birthdate!!.monthValue}/${birthdate!!.year}"
            Log.d(ContentValues.TAG, "fecha string ${dateString}")

            isMale?.let {
                isDogNeutered?.let { it1 ->

                    viewModel.addDog(
                        name = name.value,
                        birthday = dateString,
                        isMale = it,
                        isNeutered = it1,
                        isPPP = isPPP
                    )
                }
            }
            name.value = ""
            birthdate = null
            isMale = null
            isDogNeutered = null
            isNeutered = false
            isPPP = false
            selectedGender.value = genderOptions[0]
            selectedNeutered.value = neuteredOptions[0]
            
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Perro añadido",
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
                                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                            }

                            showDialog = false
                        },

                        border = BorderStroke(1.dp, Color.Transparent,
                        )
                    ) {
                        Text(text = "Confirmar", color = Color.Black)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showDialog = false
                                  },

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    state : DatePickerState
) {
    DatePicker(
        state = state,
        colors = DatePickerDefaults.colors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            weekdayContentColor = Color.Black,
            navigationContentColor = Color.Black,
            selectedDayContainerColor = Color(0xFF57B262),
            disabledSelectedDayContainerColor = Color(0xFF57B262),
            todayContentColor = Color(0xFF57B262),
            todayDateBorderColor = Color(0xFF57B262),
            dayContentColor = Color.Black,
            currentYearContentColor = Color.Black,
            selectedDayContentColor = Color.Black,
            selectedYearContainerColor = Color.Black,
            dateTextFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = Color(241, 248, 247), // color contenedor caja
                unfocusedContainerColor =  Color(241, 248, 247),
                unfocusedTextColor = Color.Black, // color del texto
                focusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                focusedIndicatorColor = Color(241, 248, 247),
                unfocusedIndicatorColor = Color(241, 248, 247),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogInputField(
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
            )},
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