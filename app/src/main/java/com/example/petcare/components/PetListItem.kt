package com.example.petcare.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.screens.home.Pet // Importando a data class Pet

@Composable
fun PetListItem(
    pet: Pet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF7F8)),
                contentAlignment = Alignment.Center
            ) {
                if (pet.photo != null) {
                    Image(
                        painter = painterResource(id = pet.photo),
                        contentDescription = pet.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pets),
                        contentDescription = "Ícone de Pet",
                        colorFilter = ColorFilter.tint(Color(0xFF26B6C4)),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = pet.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "${pet.species} ${if (!pet.breed.isNullOrEmpty()) "• ${pet.breed}" else ""}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                pet.birthYear?.let {
                    val currentYear = 2026
                    val age = currentYear - it
                    Text(text = "$age anos", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Ver detalhes",
                colorFilter = ColorFilter.tint(Color(0xFF26B6C4))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PetListItemPreview() {
    val samplePet = Pet(
        id = "1",
        name = "Theo",
        species = "Cachorro",
        breed = "Poodle",
        photo = null,
        birthYear = 2017
    )
    PetListItem(
        pet = samplePet,
        onClick = {}
    )
}
