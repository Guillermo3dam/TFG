package com.example.tfg.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tfg.screens.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(
    navController: NavHostController
){
    val context = LocalContext.current
    Column {
        NavHost(navController = navController,
            startDestination = if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
                AppScreens.LoginScreen.route
            else
                AppScreens.HomeScreen.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(route = AppScreens.LoginScreen.route) {
                BackHandler {
                    (context as ComponentActivity)?.finish()
                }
                LoginScreen(navController)
            }
            composable(route = AppScreens.HomeScreen.route) {
                BackHandler { // capturo el backhandler que tiene por defecto para que si se encuentra en esta ruta y le damos a atras la app se cierre
                    (context as ComponentActivity)?.finish()
                }
                HomeScreen(navController)
            }
            composable(route = AppScreens.AccountScreen.route) {
                AccountScreen(navController)
            }
            composable(route = AppScreens.AddDogsScreen.route) {
                AddDogScreen(navController)
            }
            composable(route = AppScreens.CalendarScreen.route) {
                CalendarScreen(navController)
            }
            composable(route = AppScreens.UpdatePasswordScreen.route) {
                UpdatePasswordScreen(navController)
            }
            composable(route = AppScreens.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(navController)
            }
            composable(route = AppScreens.NaturalRecipesScreen.route) {
                NaturalRecipesScreen(navController)
            }
            composable(route = AppScreens.SnackRecipesScreen.route) {
                SnackRecipesScreen(navController)
            }
            composable(route = AppScreens.MyDogsScreen.route) {
                MyDogsScreen(navController)
            }
            composable(route = AppScreens.SelectedDogScreen.route + "/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                SelectedDogScreen(navController = navController, id = id)
            }
            // Extrae el valor del parámetro 'id' de los argumentos del backStackEntry.
            // backStackEntry.arguments obtiene el Bundle de argumentos.
            // getString("id") intenta obtener el valor del argumento 'id'.
            // ?: "" asigna una cadena vacía si el argumento 'id' es nulo.
            // y navego a la ruta pasandole el id
            composable(route = AppScreens.AddReminderScreen.route + "/{date}") { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: ""
                AddReminderScreen(navController = navController, date = date)
            }


        }
    }
}