package com.example.pz_23_25_guselnikova_PR23106

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class AnalysesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController = navController)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = "analyses",
            ) {
                composable("Analyses") { AnalysesScreen() }
                composable("Results") { ResultsScreen() }
                composable("Support") { SupportScreen() }
                composable("Profile") { ProfileScreen() }
            }
        }
    }
}

data class PromotionItem(
    val imageResId: Int,
    val title: String,
    val description: String,
    val price: String,
    val backgroundColor: Color
)

@Composable
fun PromotionsSection(promotionItems: List<PromotionItem>) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
    ) {
        promotionItems.forEach { item ->
            PromotionCard(item)
        }
    }
}

@Composable
fun PromotionCard(item: PromotionItem) {
    Card(
        modifier = Modifier
            .padding(start=16.dp, end=8.dp)
            .height(150.dp)
            .width(270.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(item.backgroundColor)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = item.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(150.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    .width(150.dp)
            ) {
                Text(
                    text = item.title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = item.description,
                    style = TextStyle(fontSize = 14.sp, color = Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.price,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        placeholder = { Text(text = "Искать анализы", style = TextStyle(color = Color.Gray, fontSize = 18.sp)) },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFEEEEEE),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(end=16.dp, start=16.dp, top=35.dp)
            .height(50.dp)
    )
}

@Composable
fun AnalysesScreen() {

    val promotions = listOf(
        PromotionItem(
            imageResId = R.drawable.man_image,
            title = "Чек-ап для мужчин",
            description = "9 исследований",
            price = "8000 ₽",
            backgroundColor = Color(0xFF76B3FF)
        ),
        PromotionItem(
            imageResId = R.drawable.bottle_image,
            title = "Подготовка к вакцинации",
            description = "Комплексное исследование перед вакцинацией",
            price = "4000 ₽",
            backgroundColor = Color(0xFF92E9D4)

        )
    )

    val categories = listOf("Популярные", "Covid", "Комплексные")
    val analyses = listOf(
        AnalysisItem("ПЦР-тест на определение РНК коронавируса стандартный", "2 дня", "1800 ₽", 1),
        AnalysisItem("Клинический анализ крови с лейкоцитарной формулировкой", "1 день", "690 ₽", 0),
        AnalysisItem("Биохимический анализ крови, базовый", "1 день", "2440 ₽", 2),
        AnalysisItem("СОЭ (венозная кровь)", "1 день", "240 ₽", 0),
        AnalysisItem("Общий анализ мочи", "1 день", "350 ₽", 0),
        AnalysisItem("Тироксин свободный (Т4 свободный)", "1 день", "680 ₽", 0),
        AnalysisItem("Группа крови + Резус-фактор", "1 день", "750 ₽", 0)

    )
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }



    Scaffold(
        topBar = { SearchBar(searchQuery = searchQuery, onSearchQueryChange = { searchQuery = it }) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Акции и новости", color = Color(0xFF939396), fontWeight = FontWeight.Bold, fontSize = 22.sp, modifier = Modifier.padding(start = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            PromotionsSection(promotionItems = promotions)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Каталог анализов", color = Color(0xFF939396), fontWeight = FontWeight.Bold, fontSize = 22.sp, modifier = Modifier.padding(start = 16.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {

                categories.forEachIndexed { index, category ->
                    Button(
                        onClick = { selectedCategoryIndex = index },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategoryIndex == index) Color(0xFF1A6FEE) else Color(0xFFF5F5F9)
                        )

                    ) {
                        Text(category, color = if (selectedCategoryIndex == index) Color.White else Color.Black, fontSize = 20.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {

                analyses
                    .filter { it.categoryIndex == selectedCategoryIndex }
                    .filter { it.title.contains(searchQuery, ignoreCase = true) }
                    .forEach { analysis ->
                        AnalysisCard(analysis)
                    }

            }
        }
    }
}

@Composable
fun AnalysisCard(analysis: AnalysisItem) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .height(150.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end=15.dp, bottom=15.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End

            ) {
                Button(
                    onClick = { /* Add to cart */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A6FEE)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(45.dp)
                        .width(140.dp)
                ) {
                    Text("Добавить", color = Color.White, style = TextStyle(fontSize = 18.sp))
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = analysis.title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
                    .width(200.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = analysis.duration,
                    style = TextStyle(fontSize = 14.sp),
                    color = Color(0xFF939396)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = analysis.price,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    }
}

data class AnalysisItem(
    val title: String,
    val duration: String,
    val price: String,
    val categoryIndex: Int
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        val items = listOf(
            "Analyses" to R.drawable.ic_analyses,
            "Results" to R.drawable.ic_results,
            "Support" to R.drawable.ic_help,
            "Profile" to R.drawable.ic_profile
        )

        items.forEach { (route, iconRes) ->
            val isSelected = currentRoute == route
            val tint = if (isSelected) Color(0xFF1A6FEE) else Color.Gray
            val textColor = if (isSelected) Color(0xFF1A6FEE) else Color.Gray

            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = iconRes),
                        contentDescription = null,
                        tint = tint
                    )
                },
                label = {
                    Text(
                        text = route,
                        color = textColor
                    )
                },
                selected = isSelected,
                onClick = { navController.navigate(route) { launchSingleTop = true } },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun ResultsScreen() {
    Text("Результаты")
}

@Composable
fun SupportScreen() {
    Text("Поддержка")
}

@Composable
fun ProfileScreen() {
    Text("Профиль")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    AppNavigation(navController = navController)
}