package com.example.tfg.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tfg.R
import com.example.tfg.models.classes.Recipe
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NaturalRecipesScreen(navController: NavHostController) {
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
                    items(listNaturalRecipes()) { recipe ->
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



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentBottomSheet(recipe: Recipe) {
    val pagerState = rememberPagerState(0, 0F){
        3
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = recipe.title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = recipe.text,
            color = Color.Black,
            modifier = Modifier.padding(top = 6.dp),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Column {
            Tabs(pagerState)
            TabsContent(pagerState, recipe)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState, recipe: Recipe) {
    val tabs = listOf<@Composable () -> Unit>(
        { Ingredients(recipe) },
        { Utensils(recipe) },
        { Instructions(recipe) }
    )


    HorizontalPager(state = pagerState) { page ->
        tabs[page]()
    }
}

@Composable
fun Ingredients(recipe: Recipe) {
    ElaborationContent(recipe.ingredients)
}

@Composable
fun Utensils(recipe: Recipe) {
    ElaborationContent(recipe.utensils)
}

@Composable
fun Instructions(recipe: Recipe) {
    ElaborationContent(recipe.instructions)
}

@Composable
fun ElaborationContent(content: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        items(content) { item ->
            ElevatedCardContent(text = item)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val titles = listOf("Ingredientes", "Utensilios", "Elaboración")
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .height(48.dp)
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selectedContentColor = Color(0xFF57B262),
                unselectedContentColor = Color.Black,
                modifier = Modifier.background(Color.White),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        }
    }
}


@Composable
fun ElevatedCardContent(text: String) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = Color(241, 248, 247)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(6.dp),
            color = Color.Black,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ItemRecipe(recipe: Recipe, onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = Color(241, 248, 247)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(10.dp)
            .height(360.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Image(
                painter = painterResource(id = recipe.image),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = recipe.title,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(6.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = recipe.text,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Black,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4
            )
        }
    }
}

fun listNaturalRecipes(): List<Recipe> {
    return listOf(
        Recipe(
            R.drawable.cerpaza,
            "Receta Cerpaza",
            "Receta de cerdo, patatas, arroz y zanahorias. Es una receta diaria basada en cantidades para un peludo de 10Kg. Ajustar cantidades para tu peludo. Frecuencia recomendada: 1 vez por semana",
            listOf("Cerdo 90g", "Higado 30g", "Patata 80g", "Yogur 1 cucharada", "Zanahoria 40g", "Corazón de vacuno 60g", "Huevo 1 unidad", "Sardina 1 unidad"),
            listOf("Cuchillo", "Olla", "Sartén", "Cuchara"),
            listOf(
                "Lava bien la patata y las zanahorias. Pela la patata y córtala en cubos pequeños. Pela las zanahorias y córtalas en rodajas finas. Corta el hígado y la carne de cerdo en trozos pequeños, asegurándote de retirar cualquier exceso de grasa.",
                "Mientras, calienta una sartén grande a fuego medio y añade un poco de aceite de oliva. Añade el hígado y la carne de cerdo a la sartén y cocínala hasta que esté bien hecha, aproximadamente 10 minutos. Añade las rodajas de zanahoria a la sartén. Cubre y cocina a fuego medio- bajo, revolviendo ocasionalmente, hasta que estén tiernas, aproximadamente 20 minutos.",
                "Una vez que la mezcla de carne y vegetales estén cocidos, combínalos en una olla grande.",
                "Agrega la manzana y las fresas. Incorpora el resto de ingredientes. Esto añade probióticos que pueden ayudar a la salud digestiva de tu perro",
                "Revuelve para asegurar que los ingredientes se distribuyan"
            )
        ),
        Recipe(
            R.drawable.barf_pollo,
            "Receta de Barf de pollo",
            "La dieta Barf para perros es una forma natural y saludable de alimentar a tu mascota, buscando replicar la dieta que tendrían en estado salvaje, con alimentos crudos que promueven una nutrición óptima. Esta receta específica está diseñada para un perro de 10 Kg, proporcionando una mezcla equilibrada de carnes, verduras y otros nutrientes esenciales para mantener a tu perro saludable, activo y feliz. La receta se basa en carne de pollo, huevo, remolacha, espinacas, manzana y aceite de oliva.",
            listOf("Pollo 83g", "Alitas de pollo 33g", "Mollejas de 33g", "Cuello de pollo 13g", "Huevo 1 unidad"),
            listOf("Cuchillo", "Cuchara"),
            listOf(
                "Preparación de los ingredientes crudos",
                "Mezcla los ingredientes en un bol grande, mezcla las carnes crudas y la remolacha. Añade el huevo batido.",
                "Incorpora el aceite de oliva"
            )
        ),
        Recipe(
            R.drawable.ternepaza,
            "Receta Ternepaza",
            "Receta de carne de ternera, patatas, zanahorias, arroz, guisantes. Es una receta diaria basada en cantidades para un peludo de 10Kg. Ajustar cantidades para tu peludo. Frecuencia recomendada: 1 vez por semana",
            listOf("Pollo 83g", "Alitas de pollo 33g", "Mollejas de 33g", "Cuello de pollo 13g", "Huevo 1 unidad"),
            listOf("Cuchillo", "Olla", "Sartén", "Cuchara"),
            listOf(
                "Lava bien la patata y las zanahorias. Pela la patata y córtala en cubos pequeños. Pela las zanahorias y córtalas en rodajas finas. Corta el hígado y la carne de cerdo en trozos pequeños, asegurándote de retirar cualquier exceso de grasa.",
                "Mientras, calienta una sartén grande a fuego medio y añade un poco de aceite de oliva. Añade el hígado y la carne de cerdo a la sartén y cocínala hasta que esté bien hecha, aproximadamente 10 minutos. Añade las rodajas de zanahoria a la sartén. Cubre y cocina a fuego medio- bajo, revolviendo ocasionalmente, hasta que estén tiernas, aproximadamente 20 minutos.",
                "Una vez que la mezcla de carne y vegetales estén cocidos, combínalos en una olla grande.",
                "Agrega la manzana y las fresas. Incorpora el resto de ingredientes. Esto añade probióticos que pueden ayudar a la salud digestiva de tu perro",
                "Revuelve para asegurar que los ingredientes se distribuyan"
            )
        )
    )
}
