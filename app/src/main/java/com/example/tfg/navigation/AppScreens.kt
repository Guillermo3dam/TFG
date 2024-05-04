package com.example.tfg.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreens (
    val icon : ImageVector?,
    val title : String?,
    val route : String)

{
    object DogsScreen : AppScreens(null, null, "dogs_screen")
    object LoginScreen : AppScreens(null, null, "login_screen")
    object HomeScreen : AppScreens(
        Icons.Outlined.Home,
        "Inicio",
        "home_screen")
    object AccountScreen : AppScreens(
        Icons.Outlined.AccountCircle,
        "Mi cuenta",
        "account_screen")
    object CalendarScreen : AppScreens(
        Icons.Outlined.DateRange,
        "Eventos",
        "calendar_screen")

}