package com.example.petcare.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.petcare.R
import com.example.petcare.ui.components.PetListItem
import com.example.petcare.ui.components.ShortcutCard

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFFEAF7F8)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Header()

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-30).dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ShortcutCard(
                            "Lembretes",
                            R.drawable.ic_calendar_month,
                            Modifier.weight(1f)
                        )
                        ShortcutCard(
                            "Pet Shops",
                            R.drawable.ic_location_on,
                            Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            PetListItem(
                                name = "Theo",
                                breed = "Poodle",
                                age = "9 anos",
                                onClick = { navController.navigate("petDetails/1") }
                            )
                        }
                    }
                }
            }

            FloatingActionButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            )
        }
    }
}

@Composable
private fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Color(0xFF26B6C4))
            .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 60.dp)
    ) {
        Column {
            Text(
                text = "Olá, Nicolas Ferreira!",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Dê uma olhada nos seus pets",
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun FloatingActionButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FloatingActionButton(
            onClick = {},
            containerColor = Color(0xFF26B6C4),
            shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "Perfil",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        FloatingActionButton(
            onClick = {},
            containerColor = Color(0xFF26B6C4),
            shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Adicionar",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
