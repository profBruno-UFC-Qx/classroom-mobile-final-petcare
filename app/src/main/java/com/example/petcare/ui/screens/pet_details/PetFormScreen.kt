package com.example.petcare.ui.screens.pet_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun PetFormScreen(navController: NavController) {
    val name = ""
    val breed = ""
    val birthDate = ""
    val species = ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF7F8))
            .verticalScroll(rememberScrollState())
    ) {
        PetFormHeader(onBackClick = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PetPhotoPlaceholder()
            Text("Adicionar foto", fontSize = 14.sp, color = Color.Gray)

            FormField(
                label = "Nome *",
                value = name,
                placeholder = "Ex: Rex, Mimi, Luna...",
                enabled = false
            )

            FormField(
                label = "Espécie *",
                value = species,
                placeholder = "Selecione a espécie",
                trailingIconResId = R.drawable.ic_keyboard_arrow_down,
                enabled = false
            )

            FormField(
                label = "Raça",
                value = breed,
                placeholder = "Ex: Labrador, Siamês...",
                enabled = false
            )

            FormField(
                label = "Data de Nascimento",
                value = birthDate,
                placeholder = "dd/mm/aaaa",
                enabled = false
            )

            ActionButtons(
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { /* Sem interação */ }
            )
        }
    }
}

@Composable
private fun PetFormHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF26B6C4))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Voltar",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Text(
            text = "Adicionar Pet",
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PetPhotoPlaceholder() {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFD1EBEF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_alt),
                contentDescription = "Ícone de Câmera",
                modifier = Modifier.size(48.dp),
                colorFilter = ColorFilter.tint(Color(0xFF26B6C4))
            )
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF26B6C4), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_alt),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    placeholder: String,
    enabled: Boolean,
    trailingIconResId: Int? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B2D3D)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { /* Sem interação */ },
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
            trailingIcon = trailingIconResId?.let {
                {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedBorderColor = Color(0xFF26B6C4),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun ActionButtons(onCancelClick: () -> Unit, onSaveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Cancelar", color = Color.Gray)
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26B6C4)),
            shape = RoundedCornerShape(8.dp),
            enabled = false
        ) {
            Text("Salvar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetFormScreenPreview() {
    PetFormScreen(navController = rememberNavController())
}
