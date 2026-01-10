package com.example.petcare.ui.screens.reminders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.petcare.R

@Composable
fun NewReminderScreen(navController: NavController) {
    val petId = ""
    val type = ""
    val title = ""
    val description = ""
    val date = ""
    val time = ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF7F8))
            .verticalScroll(rememberScrollState())
    ) {
        NewReminderHeader(onBackClick = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormField(
                label = "Pet *",
                value = petId,
                placeholder = "Selecione o pet",
                trailingIconResId = R.drawable.ic_keyboard_arrow_down,
                enabled = false
            )

            FormField(
                label = "Tipo *",
                value = type,
                placeholder = "Saúde ou Rotina",
                trailingIconResId = R.drawable.ic_keyboard_arrow_down,
                enabled = false
            )

            FormField(
                label = "Título *",
                value = title,
                placeholder = "Ex: Banho, Vacina V10...",
                enabled = false
            )

            FormField(
                label = "Descrição",
                value = description,
                placeholder = "Informações adicionais...",
                height = 100.dp,
                enabled = false
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FormField(
                    label = "Data *",
                    value = date,
                    placeholder = "dd/mm/aaaa",
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
                FormField(
                    label = "Hora *",
                    value = time,
                    placeholder = "--:--",
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
            }

            ActionButtons(
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { /* Sem interação */ }
            )
        }
    }
}

@Composable
private fun NewReminderHeader(onBackClick: () -> Unit) {
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
            text = "Novo Lembrete",
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    placeholder: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = Dp.Unspecified,
    trailingIconResId: Int? = null
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1B2D3D)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { /* Sem interação */ },
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = height),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
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
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Cancelar", color = Color.Gray)
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91D045)),
            shape = RoundedCornerShape(8.dp),
            enabled = false
        ) {
            Text("Salvar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewReminderScreenPreview() {
    NewReminderScreen(navController = rememberNavController())
}
