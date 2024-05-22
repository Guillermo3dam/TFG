package com.example.tfg.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tfg.R
import com.example.tfg.models.classes.Recipe

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackRecipesScreen(navController: NavHostController) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
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
                        Text(text = "Recetas naturales", color = Color.Black)
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp)
                ) {
                    items(listSnackRecipes()) { recipe ->
                        ItemRecipe(recipe = recipe) {
                            selectedRecipe = recipe
                            showBottomSheet = true
                        }
                    }
                }
            }
        )

        if (showBottomSheet && selectedRecipe != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                containerColor = Color.White
            ) {
                ContentBottomSheet(recipe = selectedRecipe!!)
            }
        }
    }
}


fun listSnackRecipes(): List<Recipe> {
    return listOf(
        Recipe(
            R.drawable.snack_goma,
            "Snack de patas de pollo y zanahoria",
        "Ricas y nutritivas gomitas de snack de colágeno con patas de pollo y zxanahorias. Frecuencia recomendada: 1 vez por semana",
            listOf("Patas de pollo 3 unidades", "Zanahoria 0.5 unidades"),
            listOf("Molde congelador", "Licuadora", "Colador", "Olla", "Cuchara", "Rayador"),
            listOf(
                "Poner a hervir las patas de pollo durante 1 hora",
                "Cuando estén hervidas, colocalas en una licuadora, con un poco de líquido del mismo agua de la hervición",
                "Colocar la mezcla para eliminar cualquier residuo de huesos",
                "Rayar la zanahoria",
                "Colocar la mezcla en los moldes y llevarlo a la nevera durante aproximadamente 1 hora"
            )
        ),
        Recipe(
            R.drawable.snack_zanahoria,
            "Snack de zanahoria y plátano",
            "Ricos snacks de zanahoria, plátano y huevo. Frecuencia recomendada: 1 vez por semana",
            listOf("Zanahoria 1 unidad", "Plátano 1 unidad", "Huevo 1 unidad"),
            listOf("Horno", "Olla", "Tenedor", "Cuenco"),
            listOf(
                "Aplastamos el platano con un tenedor hasta dejarlo puré",
                "Hervimos trocitos de zanahoria y lo aplastamos con un tenedor",
                "Colocamos en un boli la zanahoria, el plátano, un huevo y la harina de avena de poco a poco y amasamos",
                "Importante que quede con una consistencia dura y formamos palitos cilíndricos y lo llevamos al horno por 30 minutos a 180 grados"
            )
        ),
        Recipe(
            R.drawable.ternepaza,
            "Snack refrescante y saludable con patas de pollo, arándanos, mango y perejil",
            "Patas de pollo, arándanos. perejil, mango",
            listOf("Patas de pollo 3 unidades", "ARándanos 1 pizca", "Mango 1 pizca", "Perejil 1 pizca"),
            listOf("Licuadora", "Colador", "Olla", "Cuchara"),
            listOf(
                "Hervimos unas patas de pollo y cuando estén listas las trituramos con un poco del líquido donde los hemos cocido. Seguidamente pasamos la mezcla por un colador para separar los restos de hueso",
                "Picamos un poco de arándano y perejil y vertemos la mezcla en un vaso",
                "Agregamos los trocitos de frutas y perejil y ponemos dentro del vaso una pata de pollo que sobresalga para que se pueda sujetar",
                "Mete los vasos al congelador por aproximadamente 2 horas"
            )
        )
    )
}
