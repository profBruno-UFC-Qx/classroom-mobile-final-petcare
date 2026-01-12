package com.example.petcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.navigation.AppNavigation
import com.example.petcare.ui.theme.PetCareTheme
import com.example.petcare.viewmodel.ThemeViewModel
import com.example.petcare.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Criamos o ViewModel que gerencia o tema
            val themeViewModel: ThemeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))

            // 2. Coletamos o estado 'isDarkTheme' do ViewModel.
            //    Este valor agora vem diretamente do DataStore.
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            // 3. A função de atualização agora chama o método do ViewModel para salvar o estado.
            val onThemeUpdated = {
                val newThemeState = !isDarkTheme
                themeViewModel.saveTheme(newThemeState)
            }

            // O resto do código permanece o mesmo.
            PetCareTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        darkTheme = isDarkTheme,
                        onThemeUpdated = onThemeUpdated
                    )
                }
            }
        }
    }
}
