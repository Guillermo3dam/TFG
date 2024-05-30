package com.example.tfg.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.viewmodels.DogState
import com.example.tfg.models.viewmodels.DogViewModel
import com.example.tfg.models.viewmodels.ReminderViewModel
import com.example.tfg.navigation.AppScreens


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyDogsScreen(
    navController: NavController,
    viewModel: DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Inicia la carga de datos
    LaunchedEffect(Unit) {
        viewModel.getDogs()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(Color.White),
                    colors = TopAppBarColors(
                        Color.White,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    ),
                    title = {
                        Text(text = "Mis perros", color = Color.Black)
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
            content = {
                when (val result = viewModel.state.value) {
                    is DogState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp)
                        ) {

                            items(result.data) { dog ->
                                Column (
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
                                ){
                                    ItemDog(
                                        dog = dog,
                                        isSelected = false,
                                        onClick = {
                                            Log.d("DogListScreen", "Navigating to selected dog screen for dog: ${dog.name}")
                                            navController.navigate(AppScreens.SelectedDogScreen.route + "/${dog.id}")
                                        },
                                        widthModifier = Modifier.fillMaxWidth()
                                    )
                                }

                            }
                        }
                    }
                    is DogState.Failure -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Text(text = result.message)
                            }
                        }
                    }
                    is DogState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Text(text = "Cargando..")
                            }
                        }
                    }
                    is DogState.Empty -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "No se han encontrado perros")
                            }
                        }
                    }
                }
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ItemDog(
    dog: Dog,
    isSelected: Boolean,
    dogViewModel: DogViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    reminderViewModel: ReminderViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onClick: () -> Unit,
    widthModifier: Modifier = Modifier.fillMaxWidth(),
    showDeleteIcon: Boolean = true
) {
    val defaultColor = Color(0xFFF1F8F7)
    val selectedColor = Color(0xFF57B262)

    val showDialog = remember { mutableStateOf(false) }

    // Función para mostrar el diálogo de confirmación
    fun showDeleteConfirmationDialog() {
        showDialog.value = true
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = if (isSelected) selectedColor else defaultColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .then(widthModifier)
            .height(60.dp),
        onClick = {
            Log.d("ItemDog", "Card clicked for dog: ${dog.name}")
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = dog.name,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (showDeleteIcon) {
                IconButton(
                    onClick = { showDeleteConfirmationDialog() },
                    modifier = Modifier.clickable { /* No action on click */ }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }

    if (showDialog.value) {
        MyDialog(
            title = "Eliminar perro",
            option = "¡¡ATENCIÓN!! Se borrará toda la información de tu perro incluidos sus recordatorios de forma PERMANENTE. ¿Seguro de que deseas eliminar a ${dog.name.uppercase()}?",
            onDismiss = { showDialog.value = false },
            onConfirm = {
                dogViewModel.deleteDog(dog.id, reminderViewModel)
                showDialog.value = false
            },
            color = Color.Red
        )
    }
}


