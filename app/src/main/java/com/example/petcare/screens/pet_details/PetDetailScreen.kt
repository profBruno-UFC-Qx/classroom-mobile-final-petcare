package com.example.petcare.screens.pet_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.petcare.R
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.local.entity.PetEventEntity
import com.example.petcare.viewmodel.PetDetailViewModel
import com.example.petcare.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    navController: NavController,
    petId: Long
) {
    val factory = ViewModelFactory(LocalContext.current, petId = petId)
    val viewModel: PetDetailViewModel = viewModel(factory = factory)

    val pet by viewModel.pet.collectAsState()
    val events by viewModel.events.collectAsState()

    val showDeleteDialog = remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("event_form/$petId") },
                containerColor = Color(0xFF26B6C4),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Cuidado")
            }
        }
    ) { innerPadding ->
        if (pet == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF26B6C4))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFFF8FEFF))
            ) {
                PetDetailsHeader(
                    pet = pet!!,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { navController.navigate("pet_form?petId=$petId") },
                    onDeleteClick = { showDeleteDialog.value = true }
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Próximos Cuidados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B2D3D),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (events.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Nenhum agendamento encontrado.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(events) { event ->
                                EventItem(event = event, dateFormatter = dateFormatter)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Você tem certeza que deseja excluir '${pet?.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pet?.let { viewModel.deletePet(it) }
                        showDeleteDialog.value = false
                        navController.popBackStack()
                    }
                ) { Text("Excluir", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun EventItem(event: PetEventEntity, dateFormatter: SimpleDateFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF0F9FA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = when (event.type) {
                            "Vacina" -> R.drawable.ic_heart
                            "Banho e Tosa" -> R.drawable.ic_pets
                            else -> R.drawable.ic_event
                        }
                    ),
                    contentDescription = null,
                    tint = Color(0xFF26B6C4),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1B2D3D)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Data: ${dateFormatter.format(event.date)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.time,
                        fontSize = 14.sp,
                        color = Color(0xFF26B6C4),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = Color(0xFFF0F9FA),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = event.type,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF26B6C4)
                )
            }
        }
    }
}

@Composable
private fun PetDetailsHeader(
    pet: PetEntity,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val ageText = pet.birthDate?.let { birthDate ->
        val birthCalendar = Calendar.getInstance().apply { time = birthDate }
        val currentCalendar = Calendar.getInstance()
        var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        if (age < 1) "Menos de 1 ano" else "$age anos"
    } ?: "Idade não informada"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Color(0xFF26B6C4))
            .padding(bottom = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = "Editar", tint = Color.White)
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(painter = painterResource(id = R.drawable.ic_delete), contentDescription = "Deletar", tint = Color.White)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(pet.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${pet.species}${pet.breed?.takeIf { it.isNotBlank() }?.let { " • $it" } ?: ""}",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(ageText, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }
        }
    }
}