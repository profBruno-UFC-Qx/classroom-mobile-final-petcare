package com.example.petcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.petcare.R
import com.example.petcare.ui.screens.reminders.ReminderItem

@Composable
fun ReminderCard(
    reminder: ReminderItem,
    isCompleted: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
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
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF26B6C4)
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

            IconButton(onClick = onDeleteClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Deletar",
                    colorFilter = ColorFilter.tint(Color.Red.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReminderCardPreview() {
    val reminder = ReminderItem("1", "Dar remédio", "2 gotas", "10/01/2026", "08:00", "health")
    ReminderCard(reminder = reminder, onCheckedChange = {}, onDeleteClick = {})
}
