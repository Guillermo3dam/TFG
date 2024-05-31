package com.example.tfg.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.classes.Reminder
import com.example.tfg.models.viewmodels.ReminderViewModel
import com.example.tfg.models.viewmodels.ReminderState
import com.example.tfg.models.viewmodels.DogViewModel
import com.example.tfg.models.viewmodels.DogState
import com.example.tfg.navigation.AppScreens
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar1Screen(
    navController: NavController,
    reminderViewModel: ReminderViewModel = viewModel(),
    dogViewModel: DogViewModel = viewModel()
) {
    val state = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var date by remember { mutableStateOf<String?>(null) }
    var showNoDogsAlert by remember { mutableStateOf(false) }
    var showNoDateAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        reminderViewModel.getReminders()
        dogViewModel.getDogs()
    }

    LaunchedEffect(state.selectedDateMillis) {
        val millis = state.selectedDateMillis
        if (millis != null) {
            selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
            date = "${selectedDate!!.dayOfMonth}/${selectedDate!!.monthValue}/${selectedDate!!.year}"
            Log.d("Calendar1Screen", "Fecha seleccionada: $date")
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF57B262),
                onClick = {
                    val dogState = dogViewModel.state.value
                    Log.d("Calendar1Screen", "DogState: $dogState, Date: $date")

                    when (dogState) {
                        is DogState.Success -> {
                            if (date == null) {
                                showNoDateAlert = true
                            } else {
                                val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())
                                navController.navigate(AppScreens.AddReminderScreen.route + "/$encodedDate")
                            }
                        }
                        is DogState.Empty, is DogState.Failure -> {
                            showNoDogsAlert = true
                        }
                        is DogState.Loading -> {
                            // Optional: Handle loading state if necessary
                        }
                    }
                },
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 72.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                DatePicker(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),  // Reducir el padding inferior del calendario
                    title = null,
                    headline = null,
                    showModeToggle = false,
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
                    ),
                )

                when (val result = reminderViewModel.state.value) {
                    is ReminderState.Success -> {
                        Row(
                            modifier = Modifier.padding(start = 10.dp, bottom = 6.dp)
                        ){
                            Text(text = "Mostrando listas de recordatorios")

                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 320.dp)
                                .padding(start = 10.dp, end = 10.dp, bottom = 72.dp)
                        ) {
                            items(result.data) { reminder ->
                                ReminderCard(reminder, dogViewModel){
                                    reminderViewModel.deleteReminderFromCurrentUser(reminder)
                                }
                            }
                        }
                    }
                    is ReminderState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(bottom = 70.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(text = "Cargando recordatorios..")

                            }
                        }
                    }
                    is ReminderState.Failure -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(bottom = 70.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(text = result.message)

                            }
                        }
                    }
                    is ReminderState.Empty -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(bottom = 70.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(text = "No se han encontrado recordatorios")

                            }
                        }
                    }
                }
            }
        }
    )

    if (showNoDogsAlert) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showNoDogsAlert = false },
            confirmButton = {
                TextButton(onClick = { showNoDogsAlert = false }) {
                    Text("OK")
                }
            },
            title = { Text("No tienes ningun perro") },
            text = { Text("Antes de añadir un recordatorio debes añadir tu perro.") }
        )
    }

    if (showNoDateAlert) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showNoDateAlert = false },
            confirmButton = {
                TextButton(onClick = { showNoDateAlert = false }) {
                    Text("OK")
                }
            },
            title = { Text("Fecha no seleccionada") },
            text = { Text("Por favor, selecciona una fecha antes de continuar.") }
        )
    }
}

@Composable
fun ReminderCard(
    reminder: Reminder,
    dogViewModel: DogViewModel = viewModel(),
    onDeleteClick: () -> Unit // Añadimos este parámetro
) {
    var dog by remember { mutableStateOf<Dog?>(null) }

    LaunchedEffect(reminder.dogId) {
        dogViewModel.getDogById(reminder.dogId) {
            dog = it
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFF1F8F7),
            contentColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (dog != null) "${dog!!.name.uppercase()}" else "Cargando información del perro...",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${reminder.date} ${reminder.hour}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = reminder.description,
                style = MaterialTheme.typography.bodyMedium
            )

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}
