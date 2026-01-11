package com.example.petcare.screens.pet_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.petcare.viewmodel.PetFormViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormScreen(
    navController: NavController,
    viewModel: PetFormViewModel
) {
    val uiState = viewModel.uiState
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Novo Pet") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.savePet()
                    navController.popBackStack()
                },
                containerColor = if (uiState.isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text("Salvar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Nome do Pet*") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.name.isBlank()
            )

            OutlinedTextField(
                value = uiState.species,
                onValueChange = viewModel::onSpeciesChange,
                label = { Text("Espécie* (Ex: Cachorro, Gato)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.species.isBlank()
            )

            OutlinedTextField(
                value = uiState.breed,
                onValueChange = viewModel::onBreedChange,
                label = { Text("Raça (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.birthDate?.let { dateFormat.format(it) } ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text("Data de Nascimento*") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.birthDate == null
            )

            Button(onClick = { viewModel.onBirthDateChange(Date()) }) {
                Text("Definir Data de Hoje (Temporário)")
            }

            Text("*Campos obrigatórios", style = MaterialTheme.typography.bodySmall)
        }
    }
}
