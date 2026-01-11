package com.example.petcare.screens.petshop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

data class PetShop(
    val id: String,
    val name: String,
    val address: String,
    val distance: String,
    val rating: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetShopsScreen(navController: NavController) {
    val petShops = remember {
        listOf(
            PetShop("1", "Mundo Pet Feliz", "Rua das Flores, 123", "1.2 km", 4.8f),
            PetShop("2", "Amigo Fiel Pet Shop", "Avenida dos Animais, 456", "2.5 km", 4.5f),
            PetShop("3", "Central do Bicho", "Praça da Matriz, 789", "3.1 km", 4.9f),
            PetShop("4", "Patas & Cia", "Rodovia Principal, km 10", "5.8 km", 4.2f)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pet Shops Próximos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(petShops) { shop ->
                PetShopListItem(shop = shop)
            }
        }
    }
}

@Composable
private fun PetShopListItem(shop: PetShop) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_location_on),
                contentDescription = "Ícone de localização",
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = shop.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = shop.address, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Avaliação",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFC107)
                    )
                    Text(
                        text = " ${shop.rating} • ${shop.distance}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PetShopsScreenPreview() {
    PetShopsScreen(navController = rememberNavController())
}
