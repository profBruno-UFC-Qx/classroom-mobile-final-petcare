package com.example.petcare.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Estado para controlar a visibilidade e disparar as animações
    var isVisible by remember { mutableStateOf(false) }

    // Animação de escala mais simples e declarativa
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    // Efeito que executa uma única vez quando o componente entra na tela
    LaunchedEffect(Unit) {
        isVisible = true // Inicia as animações
        delay(2500) // Espera 2.5 segundos
        // Navega para a tela de login, limpando a pilha de navegação
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF26B6C4)), // Cor padrão do PetCare
        contentAlignment = Alignment.Center
    ) {
        // Coluna principal que será escalada
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.scale(scale)
        ) {
            // --- CÍRCULO DO LOGO ---
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Usando o Icon padronizado para consistência
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "PetCare Logo",
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF26B6C4)
                    )
                }
            }

            // --- TEXTO PRINCIPAL com animação de fade e slide ---
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = 200)) +
                        slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Text(
                    text = "PetCare",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- SUBTÍTULO com animação de fade ---
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = 400))
            ) {
                Text(
                    text = "Cuidando do seu melhor amigo",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // Usamos um NavController "falso" para o preview funcionar
    SplashScreen(navController = rememberNavController())
}
