package com.example.petcare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petcare.data.repository.AuthRepository
import com.example.petcare.viewmodel.ViewModelFactory
import com.example.petcare.screens.SplashScreen
import com.example.petcare.screens.auth.LoginScreen
import com.example.petcare.screens.auth.SignUpScreen
import com.example.petcare.screens.home.HomeScreen
import com.example.petcare.screens.pet_details.PetFormScreen
import com.example.petcare.screens.pet_details.PetDetailScreen
import com.example.petcare.screens.petshop.PetShopsScreen
import com.example.petcare.screens.profile.ProfileScreen
import com.example.petcare.screens.reminders.RemindersScreen
import com.example.petcare.viewmodel.HomeViewModel
import com.example.petcare.viewmodel.PetFormViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }
    val context = LocalContext.current
    val factory = remember { ViewModelFactory(context) }


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
            val homeViewModel: HomeViewModel = viewModel(factory = factory)

            HomeScreen(
                navController = navController,
                userName = currentUser?.displayName,
                homeViewModel = homeViewModel
            )
        }

        composable("pet_form") {
            val petFormViewModel: PetFormViewModel = viewModel(factory = factory)
            PetFormScreen(
                navController = navController,
                viewModel = petFormViewModel
            )
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
