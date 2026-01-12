package com.example.petcare.screens.event_form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.petcare.components.DropdownMenuField
import com.example.petcare.viewmodel.EventFormViewModel
import com.example.petcare.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(
    navController: NavController,
    petId: Long
) {
    val factory = ViewModelFactory(LocalContext.current, petId = petId)
    val viewModel: EventFormViewModel = viewModel(factory = factory)
    val eventTypes = listOf("Vacina", "Banho e Tosa", "Consulta", "Vermífugo", "Outro")
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Estados para os diálogos
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Configuração do DatePicker
    val datePickerState = rememberDatePickerState()

    // Configuração do TimePicker (Relógio)
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    // --- DIÁLOGO DO CALENDÁRIO ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onNextDueDateChange(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // --- DIÁLOGO DO RELÓGIO (TIMEPICKER) ---
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    viewModel.onTimeChange(formattedTime)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cuidado") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
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
            DropdownMenuField(
                label = "Tipo de Cuidado",
                options = eventTypes,
                selectedOption = viewModel.eventType,
                onOptionSelected = { viewModel.onEventTypeChange(it) }
            )

            OutlinedTextField(
                value = viewModel.eventName,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nome do Cuidado *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Campo de Data (Abre Calendário)
            CustomClickableField(
                label = "Data do Agendamento *",
                value = viewModel.nextDueDate?.let { dateFormatter.format(it) } ?: "Selecionar data",
                onClick = { showDatePicker = true }
            )

            // Campo de Hora (Abre Relógio)
            CustomClickableField(
                label = "Horário *",
                value = viewModel.eventTime.ifBlank { "Selecionar horário" },
                onClick = { showTimePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.saveEvent()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26B6C4)),
                enabled = viewModel.eventName.isNotBlank() &&
                        viewModel.nextDueDate != null &&
                        viewModel.eventTime.isNotBlank()
            ) {
                Text("Confirmar Agendamento", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CustomClickableField(label: String, value: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).clickable { onClick() },
            color = Color(0xFFF7FBFB),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = value, color = if (value.contains(":|/".toRegex())) Color.Black else Color.Gray)
            }
        }
    }
}