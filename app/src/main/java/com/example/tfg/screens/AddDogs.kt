package com.example.tfg.screens

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogScreen(
    NavController: NavController
) {
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
                        IconButton(onClick = { NavController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                )
            }
        ) {
            AddDogForm(Modifier.padding(it))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogForm(modifier: Modifier = Modifier) {

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

    var allOptionsSelected by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "¿Como se llama?",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        DogInputField(valueState = name, labelId = "Nombre del perro", keyboardType = KeyboardType.Text)

        Text(
            text = "¿Cual es su fecha de nacimiento?",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(241, 248, 247),
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Transparent)
        ) {
            Text(
                text = if (birthdate != null) {
                    "Fecha: ${birthdate!!.dayOfMonth}/${birthdate!!.monthValue}/${birthdate!!.year}"
                } else {
                    "Seleccionar la fecha"
                },
                color = Color.Black
            )
        }

        Text(
            text = "¿Es macho o hembra?",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            genderOptions.forEach { option ->
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = option == selectedGender.value,
                        onClick = { selectedGender.value = option },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        color = Color.Black,
                        modifier = Modifier.width(80.dp) // Ancho fijo para los textos
                    )
                }
            }
        }

        Text(
            text = "¿Está castrado?",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            neuteredOptions.forEach { option ->
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = option == selectedNeutered.value,
                        onClick = { selectedNeutered.value = option },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        color = Color.Black,
                        modifier = Modifier.width(80.dp) // Ancho fijo para los textos
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
                    checkedColor = Color(241, 248, 247),
                    uncheckedColor = Color.Black
                )
            )
            Text(
                "¿Es Potencialmente Peligroso?",
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        allOptionsSelected = selectedGender.value.isNotBlank() && selectedNeutered.value.isNotBlank() && name.value.isNotBlank() && birthdate!=null

        SubmitButton(textId = "Añadir perro", inputValido = allOptionsSelected) {

        }



        if (showDialog) {
            DatePickerDialog(
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                ),
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(        colors = ButtonDefaults.buttonColors(
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
                        onClick = { showDialog = false },

                        border = BorderStroke(1.dp, Color.Transparent)
                    ) {
                        Text(text = "Cancelar", color = Color.Black)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                DatePicker(state = state,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                        weekdayContentColor = Color.Black,
                        subheadContentColor = Color.Black,
                        navigationContentColor = Color.Black,
                        yearContentColor = Color.Black,
                        selectedDayContainerColor = Color(0xFF57B262),
                        disabledSelectedDayContainerColor = Color(0xFF57B262),
                        todayContentColor = Color(0xFF57B262),
                        todayDateBorderColor = Color(0xFF57B262),
                        dayContentColor = Color.Black,
                        currentYearContentColor = Color.Black,
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
        }
    }
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
        placeholder = { Text(text = labelId, color = Color.Black) },
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