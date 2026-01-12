package com.example.petcare.screens.pet_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.petcare.components.DropdownMenuField
import com.example.petcare.screens.event_form.CustomClickableField
import com.example.petcare.viewmodel.PetFormViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormScreen(
    navController: NavController,
    viewModel: PetFormViewModel,
    petId: Long?
) {
    LaunchedEffect(key1 = petId) {
        petId?.let { id ->
            if (id > 0L) {
                viewModel.loadPetForEditing(id)
            }
        }
    }

    val uiState = viewModel.uiState
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val speciesOptions = listOf("Cachorro", "Gato", "Pássaro", "Coelho", "Hamster", "Outro")

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onBirthDateChange(java.util.Date(it))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Editar Pet" else "Adicionar Novo Pet") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Nome do Pet*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            DropdownMenuField(
                label = "Espécie*",
                options = speciesOptions,
                selectedOption = uiState.species,
                onOptionSelected = { viewModel.onSpeciesChange(it) }
            )

            OutlinedTextField(
                value = uiState.breed,
                onValueChange = viewModel::onBreedChange,
                label = { Text("Raça (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            CustomClickableField(
                label = "Data de Nascimento*",
                value = uiState.birthDate?.let { dateFormat.format(it) } ?: "Selecionar data",
                onClick = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.savePet()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF26B6C4)
                ),
                enabled = uiState.isFormValid
            ) {
                Text(
                    text = if (uiState.isEditing) "Atualizar Pet" else "Salvar Pet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Text(
                "* Campos obrigatórios",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
