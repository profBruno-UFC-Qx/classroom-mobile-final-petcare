package com.example.petcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petcare.data.repository.AuthRepository
import com.example.petcare.screens.home.HomeScreen
import com.example.petcare.screens.SplashScreen
import com.example.petcare.screens.auth.LoginScreen
import com.example.petcare.screens.auth.SignUpScreen
import com.example.petcare.screens.pet_details.PetDetailScreen
import com.example.petcare.screens.pet_details.PetFormScreen
import com.example.petcare.screens.petshop.PetShopsScreen
import com.example.petcare.screens.profile.ProfileScreen
import com.example.petcare.screens.reminders.RemindersScreen
import com.example.petcare.ui.theme.PetCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetCareTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }


    NavHost(
        navController = navController,
        startDestination = if (authRepository.getCurrentUser()  != null) "home" else "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("signup") {
            SignUpScreen(navController = navController)
        }

        composable("home") {
            val currentUser = authRepository.getCurrentUser()
            HomeScreen(
                navController = navController,
                userName = currentUser?.displayName
            )
        }

        composable("pet_form") {
            PetFormScreen(navController = navController)
        }

        composable("reminders") {
            RemindersScreen(navController = navController)
        }

        composable("profile") {
            val currentUser = authRepository.getCurrentUser()
            ProfileScreen(
                navController = navController,
                authRepository = authRepository,
                userName = currentUser?.displayName,
                userEmail = currentUser?.email
            )
        }

        composable("pet_detail/{petId}") {
            PetDetailScreen(navController = navController)
        }

        composable("petshop") {
            PetShopsScreen(navController = navController)
        }
    }
}
