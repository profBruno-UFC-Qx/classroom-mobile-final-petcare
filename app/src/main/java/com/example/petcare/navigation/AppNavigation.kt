package com.example.petcare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petcare.data.repository.AuthRepository
import com.example.petcare.screens.SplashScreen
import com.example.petcare.screens.auth.LoginScreen
import com.example.petcare.screens.auth.SignUpScreen
import com.example.petcare.screens.home.HomeScreen
import com.example.petcare.screens.pet_details.PetDetailScreen
import com.example.petcare.screens.pet_details.PetFormScreen
import com.example.petcare.screens.petshop.PetShopsScreen
import com.example.petcare.screens.profile.ProfileScreen
import com.example.petcare.screens.reminders.RemindersScreen
import com.example.petcare.viewmodel.HomeViewModel
import com.example.petcare.viewmodel.PetDetailViewModel
import com.example.petcare.viewmodel.PetFormViewModel
import com.example.petcare.viewmodel.ViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }
    val context = LocalContext.current
    val factory = remember { ViewModelFactory(context) }


    NavHost(
        navController = navController,
        startDestination = if (authRepository.getCurrentUser() != null) "home" else "splash"
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

        composable(
            route = "pet_form?petId={petId}",
            arguments = listOf(
                navArgument("petId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val petFormViewModel: PetFormViewModel = viewModel(factory = factory)
            val petId = backStackEntry.arguments?.getLong("petId")

            PetFormScreen(
                navController = navController,
                viewModel = petFormViewModel,
                petId = if (petId == 0L) null else petId
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

        composable(
            route = "pet_detail/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.LongType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getLong("petId")
                ?: throw IllegalStateException("Pet ID não encontrado. Verifique a rota de navegação.")

            val viewModel: PetDetailViewModel = viewModel(factory = factory)

            PetDetailScreen(
                navController = navController,
                petId = petId,
                viewModel = viewModel
            )
        }

        composable("petshop") {
            PetShopsScreen(navController = navController)
        }
    }
}
