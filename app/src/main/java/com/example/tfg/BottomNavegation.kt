package com.example.tfg

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.navigation.AppScreens.*
import com.example.tfg.navigation.currentRoute

@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomNavegation(
    navController: NavHostController
){
    val menu_items = listOf(
        HomeScreen,
        CalendarScreen,
        AccountScreen
    )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = Color.White
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                menu_items.forEach { item ->
                    val selected = currentRoute(navController) == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        icon = {
                            item.icon?.let {
                                Icon(imageVector = it, contentDescription = item.title)
                            }
                        },
                        label = {
                            item.title?.let { Text(text = it) }
                        }
                    )
                }

        }
    }
}
