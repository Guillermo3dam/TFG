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
                BackHandler {
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
        }
    }

}

