package com.example.petcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.datastore.ThemeDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val themeDataStore: ThemeDataStore) : ViewModel() {

    // Expõe o Flow do DataStore como um StateFlow para a UI observar.
    // Ele começa a coletar dados apenas quando a UI está ativa (SharingStarted.WhileSubscribed)
    // e mantém o último valor. O valor inicial é 'false'.
    val isDarkTheme = themeDataStore.getTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Função que a UI chama para salvar a nova preferência de tema.
    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeDataStore.saveTheme(isDarkMode)
        }
    }
}
