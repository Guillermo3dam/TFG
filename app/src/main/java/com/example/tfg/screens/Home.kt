package com.example.tfg.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tfg.R
import com.example.tfg.models.classes.Recipe
import com.example.tfg.models.viewmodels.UserViewModel
import com.example.tfg.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val auth: FirebaseAuth = Firebase.auth
    val userEmail = auth.currentUser?.email

    viewModel.getUser()
    val userName = viewModel.user.value.name

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Añadimos un padding a la fila para que no esté pegada al borde
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp) // Reducimos el tamaño del contenedor de la imagen
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {

                            }
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = Color(0xFF57B262),
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = userName,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if (userEmail != null) {
                        Text(
                            text = userEmail,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            MyPets(navController)
            Recipes(navController)
        }
    }
}


@Composable
fun MyPets(
    navController: NavHostController
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
        ){
        Text(
            text = "Mis mascotas",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f) // Esto hará que este texto ocupe tanto espacio como pueda
        )
        Spacer(modifier = Modifier.weight(1f)) // Spacer flexible para empujar el segundo texto hacia la derecha
        Text(
            text = "Todos",
            color = Color(0xFF57B262),
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable {
                    navController.navigate(route = AppScreens.MyDogsScreen.route)
                }
        )
    }
    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
        ){
        item {
            ItemPets(navController)


        }

    }
}

@Composable
fun ItemPets(
    navController: NavHostController
){
    Image(
        painter = painterResource(id = R.drawable.add_dog),
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .clickable {
                navController.navigate(route = AppScreens.AddDogsScreen.route)
            }
    )
}

@Composable
fun Recipes(
    navController: NavController
    ){
    Text(
        text = "¿Qué le apetece comer a tu peludo hoy?",
        fontWeight = FontWeight.Bold,
        color = Color.Black)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp) // Espacio entre las tarjetas
    ) {

        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color(241, 248, 247)
            ), elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate(route = AppScreens.NaturalRecipesScreen.route)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.recipes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = "Recetas caseras",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }

        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color(241, 248, 247)
            ), elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate(route = AppScreens.SnackRecipesScreen.route)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.snacks),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                )
                Text(
                    text = "Snacks caseros",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    }
}

