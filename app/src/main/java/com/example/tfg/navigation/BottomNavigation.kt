package com.example.tfg.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.tfg.navigation.AppScreens.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigation(
    navController: NavHostController
){
    val menuItems = listOf(
        HomeScreen,
        CalendarScreen,
        AccountScreen
    )

    val currentRoute = currentRoute(navController)

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            menuItems.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (isSelected)  Color(0xFF57B262) else Color.Black,
                        indicatorColor = Color.White
                    ),
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route)
                    },
                    icon = {
                        item.icon?.let { icon ->
                            Icon(
                                imageVector = icon,
                                contentDescription = item.title,
                                tint = if (isSelected)  Color(0xFF57B262) else Color.Black
                            )
                        }
                    },
                    label = {
                        item.title?.let { title ->
                            Text(
                                text = title,
                                color = if (isSelected)  Color(0xFF57B262) else Color.Black
                            )
                        }
                    }
                )
            }
        }
    }
}