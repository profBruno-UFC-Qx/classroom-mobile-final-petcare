package com.example.petcare.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ProfileScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFFEAF7F8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ProfileHeader(onBackClick = { navController.popBackStack() })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage()

                Spacer(modifier = Modifier.height(32.dp))

                UserInfoCard(name = "Nícolas Ferreira Leite", email = "nicolas@email.com")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("editProfile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91D045)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Editar Perfil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                SettingsCard()

                Spacer(modifier = Modifier.weight(1f))

                LogoutButton(onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                })
            }
        }
    }
}

@Composable
private fun ProfileHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF26B6C4))
            .padding(vertical = 8.dp, horizontal = 8.dp),
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
            text = "Meu Perfil",
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileImage() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color(0xFFD1EBEF)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription = "Foto de Perfil",
            modifier = Modifier.size(80.dp),
            colorFilter = ColorFilter.tint(Color(0xFF26B6C4))
        )
    }
}

@Composable
private fun UserInfoCard(name: String, email: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nome", fontSize = 12.sp, color = Color.Gray)
            Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "E-mail", fontSize = 12.sp, color = Color.Gray)
            Text(text = email, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SettingsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Configurações",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_light_mode),
                        contentDescription = "Ícone do modo de tema",
                        colorFilter = ColorFilter.tint(Color(0xFF26B6C4)),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Modo Noturno", fontWeight = FontWeight.Medium)
                        Text(text = "Desativado", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Switch(
                    checked = false,
                    onCheckedChange = null,
                    enabled = false
                )
            }
        }
    }
}

@Composable
private fun LogoutButton(onLogoutClick: () -> Unit) {
    OutlinedButton(
        onClick = onLogoutClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        border = BorderStroke(1.dp, Color.Red),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sair da conta", fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
