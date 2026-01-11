package com.example.petcare.screens.reminders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.petcare.R

data class ReminderItem(
    val id: String,
    val title: String,
    val description: String?,
    val date: String,
    val time: String,
    val type: String,
    val completed: Boolean = false
)

private val mockReminders = listOf(
    ReminderItem("1", "Dar remédio de verme", "2 gotas após a refeição", "03/06/2026", "08:00", "health"),
    ReminderItem("2", "Passeio matinal", null, "03/06/2026", "07:00", "routine"),
    ReminderItem("3", "Vacina V10", "Reforço anual", "10/01/2026", "14:30", "health", completed = true)
)

@Composable
fun RemindersScreen(navController: NavController) {
    val pendingReminders = mockReminders.filter { !it.completed }
    val completedReminders = mockReminders.filter { it.completed }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF7F8))
    ) {
        RemindersHeader(onBackClick = { navController.popBackStack() })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(
                    onClick = { /* Sem interação */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91D045)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = false
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Novo Lembrete",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Novo Lembrete", fontWeight = FontWeight.Bold)
                }
            }

            if (pendingReminders.isNotEmpty()) {
                item {
                    Text("Pendentes", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B2D3D))
                }
                items(pendingReminders) { reminder ->
                    ReminderCard(reminder)
                }
            }

            if (completedReminders.isNotEmpty()) {
                item {
                    Text("Concluídos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Gray)
                }
                items(completedReminders) { reminder ->
                    ReminderCard(reminder, isCompleted = true)
                }
            }

            if (mockReminders.isEmpty()) {
                item {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
private fun RemindersHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF26B6C4))
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Voltar",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Text(
                    text = "Lembretes",
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Gerencie seus lembretes de cuidados",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 48.dp)
            )
        }
    }
}

@Composable
fun ReminderCard(reminder: ReminderItem, isCompleted: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color.White.copy(alpha = 0.6f) else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(if (isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = reminder.completed,
                onCheckedChange = null,
                enabled = false,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF26B6C4),
                    disabledCheckedColor = Color(0xFF26B6C4).copy(alpha = 0.5f),
                    disabledUncheckedColor = Color.Gray.copy(alpha = 0.5f)
                )
            )

            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(
                    text = reminder.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                    color = if (isCompleted) Color.Gray else Color.Black
                )

                reminder.description?.let {
                    Text(text = it, fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar_month),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Text(text = " ${reminder.date}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_schedule),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Text(text = " ${reminder.time}", fontSize = 12.sp, color = Color.Gray)
                }

                if (!isCompleted) {
                    Surface(
                        modifier = Modifier.padding(top = 8.dp),
                        color = if (reminder.type == "health") Color(0xFF91D045).copy(alpha = 0.1f) else Color(0xFF26B6C4).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (reminder.type == "health") "Saúde" else "Rotina",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = if (reminder.type == "health") Color(0xFF91D045) else Color(0xFF26B6C4),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            IconButton(onClick = { /* Sem interação */ }, enabled = false) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Deletar",
                    colorFilter = ColorFilter.tint(Color.Red.copy(alpha = 0.3f))
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_calendar_today),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            colorFilter = ColorFilter.tint(Color(0xFF26B6C4))
        )
        Text("Nenhum lembrete cadastrado", fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 16.dp))
        Text("Adicione lembretes para seu pet", color = Color.Gray, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun RemindersScreenPreview() {
    RemindersScreen(navController = rememberNavController())
}
