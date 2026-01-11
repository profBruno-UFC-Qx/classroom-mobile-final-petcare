package com.example.petcare.screens.pet_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import com.example.petcare.R
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.viewmodel.PetDetailViewModel
import java.util.Calendar

@Composable
fun PetDetailScreen(
    navController: NavController,
    petId: Long,
    viewModel: PetDetailViewModel
) {
    LaunchedEffect(key1 = petId) {
        viewModel.loadPet(petId)
    }

    val pet by viewModel.pet.collectAsState()
    val showDeleteDialog = remember { mutableStateOf(false) }

    val selectedTab = 0
    val tabs = listOf("Vacinas", "Banho e Tosa", "Consultas")

    Scaffold(
        containerColor = Color(0xFFEAF7F8)
    ) { innerPadding ->
        if (pet == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                PetDetailsHeader(
                    pet = pet!!,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = {
                        navController.navigate("pet_form?petId=${pet!!.id}")
                    },
                    onDeleteClick = { showDeleteDialog.value = true }
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFF26B6C4),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFF26B6C4)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { /* Sem interação */ },
                            text = {
                                Text(
                                    title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = { /* Sem interação */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91D045)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Adicionar",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adicionar ${tabs[selectedTab].takeWhile { it != ' ' }}")
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item { VaccineCard("Antirrábica", "03/06/2024", "Vacina") }
                        item { VaccineCard("V10", "15/01/2024", "Vacina") }
                    }
                }
            }
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Você tem certeza que deseja excluir '${pet?.name}'? Esta ação não pode ser desfeita.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletePet(petId)
                        showDeleteDialog.value = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Voltar",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Row {
                    IconButton(onClick = onEditClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Editar",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "Deletar",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = "Ícone de Pet",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(Color.White)
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


@Composable
fun VaccineCard(name: String, date: String, type: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(
                    color = Color(0xFF91D045).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color(0xFF91D045),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aplicada em: $date", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun SimpleRecordCard(title: String, subtitle: String, date: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(subtitle, color = Color.Gray, fontSize = 14.sp)
            Text(date, color = Color(0xFF26B6C4), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetDetailScreenPreview() {
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            Text("Preview indisponível devido à dependência de ViewModel")
        }
    }
}
