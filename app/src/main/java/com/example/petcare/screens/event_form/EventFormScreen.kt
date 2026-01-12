package com.example.petcare.screens.event_form

import android.Manifest
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.petcare.R
import com.example.petcare.components.DropdownMenuField
import com.example.petcare.utils.NotificationWorker
import com.example.petcare.viewmodel.EventFormViewModel
import com.example.petcare.viewmodel.ViewModelFactory
import com.google.accompanist.permissions.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EventFormScreen(
    navController: NavController,
    petId: Long
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context, petId = petId)
    val viewModel: EventFormViewModel = viewModel(factory = factory)

    val eventTypes = listOf("Vacina", "Banho e Tosa", "Consulta", "Vermífugo", "Outro")
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else { null }

    LaunchedEffect(Unit) {
        notificationPermissionState?.let {
            if (!it.status.isGranted) {
                it.launchPermissionRequest()
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onNextDueDateChange(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        TimePickerDialogCustom(
            onCancel = { showTimePicker = false },
            onConfirm = {
                val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                viewModel.onTimeChange(formattedTime)
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cuidado") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
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

            CustomClickableField(
                label = "Data do Agendamento *",
                value = viewModel.nextDueDate?.let { dateFormatter.format(it) } ?: "Selecionar data",
                onClick = { showDatePicker = true }
            )

            CustomClickableField(
                label = "Horário *",
                value = viewModel.eventTime.ifBlank { "Selecionar horário" },
                onClick = { showTimePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val eventCalendar = Calendar.getInstance().apply {
                        timeInMillis = viewModel.nextDueDate ?: System.currentTimeMillis()
                        val timeParts = viewModel.eventTime.split(":")
                        set(Calendar.HOUR_OF_DAY, timeParts.getOrNull(0)?.toInt() ?: 0)
                        set(Calendar.MINUTE, timeParts.getOrNull(1)?.toInt() ?: 0)
                        set(Calendar.SECOND, 0)
                    }

                    val delay = eventCalendar.timeInMillis - System.currentTimeMillis()

                    if (delay > 0) {
                        val inputData = workDataOf(
                            "title" to "Lembrete: ${viewModel.eventName}",
                            "message" to "Está na hora do compromisso do seu pet!",
                            "id" to eventCalendar.timeInMillis.toInt()
                        )

                        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .setInputData(inputData)
                            .addTag("event_${eventCalendar.timeInMillis}")
                            .build()

                        WorkManager.getInstance(context).enqueue(notificationWorkRequest)
                    }

                    viewModel.saveEvent()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = viewModel.eventName.isNotBlank() &&
                        viewModel.nextDueDate != null &&
                        viewModel.eventTime.isNotBlank()
            ) {
                Text("Confirmar Agendamento", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CustomClickableField(label: String, value: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = value, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun TimePickerDialogCustom(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selecione o Horário",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(modifier = Modifier.padding(top = 16.dp).fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) { Text("Cancelar") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}