package com.example.petcare.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.petcare.components.PetListItem
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    userName: String?,
    homeViewModel: HomeViewModel
) {
    val pets by homeViewModel.pets.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FEFF))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color(0xFF26B6C4))
                    .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 60.dp)
            ) {
                Column {
                    Text(
                        text = "Olá, ${userName ?: "Dono(a) de Pet"}!",
                        fontSize = 32.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Dê uma olhada nos seus pets",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-30).dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        title = "Lembretes",
                        imageRes = R.drawable.ic_calendar_month,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("reminders") }
                    )
                    ActionCard(
                        title = "Pet Shops",
                        imageRes = R.drawable.ic_location_on,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("petshop") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (pets.isEmpty()) {
                    EmptyStateCard()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(pets) { pet ->
                            PetListItem(
                                pet = pet,
                                onClick = {  navController.navigate("pet_detail/${pet.id}") }
                            )
                        }
                    }
                }
            }
        }

        SmallFloatingButton(
            imageRes = R.drawable.ic_person,
            alignment = Alignment.BottomStart,
            onClick = { navController.navigate("profile") }
        )

        SmallFloatingButton(
            imageRes = R.drawable.ic_add,
            alignment = Alignment.BottomEnd,
            onClick = { navController.navigate("pet_form") }
        )
    }
}

// 3. REMOVIDO: A função PetListItem duplicada que estava aqui foi apagada.

@Composable
fun ActionCard(title: String, imageRes: Int, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(Color(0xFF26B6C4))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 14.sp, color = Color(0xFF1B2D3D))
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF1F1F1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pets),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF26B6C4))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Nenhum pet cadastrado", fontWeight = FontWeight.Bold)
            Text("Adicione seu primeiro animal", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun BoxScope.SmallFloatingButton(imageRes: Int, alignment: Alignment, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .align(alignment)
            .padding(24.dp)
            .size(64.dp),
        containerColor = Color(0xFF26B6C4),
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}
