package com.example.tfg.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tfg.navigation.AppScreens
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar1Screen(navController: NavController) {
    val state = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }


    var date by remember { mutableStateOf<String?>(null) }
    // Observa cambios en el estado del DatePicker
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
                onClick = {
                    val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())
                    navController.navigate(AppScreens.AddReminderScreen.route + "/$encodedDate")
                },
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 72.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                DatePicker(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
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
                    )
                )
            }
        }
    )
}