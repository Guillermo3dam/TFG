package com.example.tfg.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tfg.screens.AccountScreen
import com.example.tfg.screens.CalendarScreen
import com.example.tfg.screens.DogsScreen
import com.example.tfg.screens.HomeScreen
import com.example.tfg.screens.LoginScreen

@Composable
fun AppNavigation(
    navController: NavHostController
){
    Column {
        NavHost(navController = navController,
            startDestination = AppScreens.LoginScreen.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(route = AppScreens.LoginScreen.route) {
                LoginScreen(navController)
            }
            composable(route = AppScreens.HomeScreen.route) {
                HomeScreen(navController)
            }
            composable(route = AppScreens.AccountScreen.route) {
                AccountScreen(navController)
            }
            composable(route = AppScreens.DogsScreen.route) {
                DogsScreen(navController)
            }
            composable(route = AppScreens.CalendarScreen.route) {
                CalendarScreen(navController)
            }
        }
    }

}

