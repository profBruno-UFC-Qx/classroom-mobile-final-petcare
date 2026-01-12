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
import com.example.petcare.screens.event_form.EventFormScreen
import com.example.petcare.screens.home.HomeScreen
import com.example.petcare.screens.pet_details.PetDetailScreen
import com.example.petcare.screens.pet_details.PetFormScreen
import com.example.petcare.screens.petshop.PetShopScreen
import com.example.petcare.screens.profile.ProfileScreen
import com.example.petcare.viewmodel.HomeViewModel
import com.example.petcare.viewmodel.PetFormViewModel
import com.example.petcare.viewmodel.ViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (authRepository.getCurrentUser() != null) "home" else "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }

        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory(context))
            HomeScreen(
                navController = navController,
                userName = authRepository.getCurrentUser()?.displayName,
                homeViewModel = homeViewModel
            )
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

        composable("petshop") {
            PetShopScreen(navController = navController)
        }

        composable(
            route = "pet_form?petId={petId}",
            arguments = listOf(navArgument("petId") { type = NavType.LongType; defaultValue = 0L })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getLong("petId")
            val petFormViewModel: PetFormViewModel = viewModel(factory = ViewModelFactory(context))
            PetFormScreen(
                navController = navController,
                viewModel = petFormViewModel,
                petId = if (petId == 0L) null else petId
            )
        }

        composable(
            route = "pet_details/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.LongType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getLong("petId")!!
            PetDetailScreen(
                navController = navController,
                petId = petId
            )
        }

        composable(
            route = "event_form/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.LongType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getLong("petId")
                ?: throw IllegalStateException("petId é obrigatório para o formulário de evento")

            EventFormScreen(
                navController = navController,
                petId = petId
            )
        }
    }
}
