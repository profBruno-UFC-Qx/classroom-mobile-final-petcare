package com.example.petcare.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepository {

    // Obtém a instância principal do Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //Função para criar um novo usuário com e-mail e senha.
    suspend fun createUser(name: String, email: String, password: String) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            authResult.user?.updateProfile(profileUpdates)?.await()

        } catch (e: Exception) {
            throw e
        }
    }

    // Função para autenticar um usuário com e-mail e senha.
    suspend fun loginUser(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw e
        }
    }

    //Função para fazer logout do usuário atualmente conectado.
    fun logout() {
        firebaseAuth.signOut()
    }

    // Retorna o usuário atualmente logado no Firebase. Pode ser nulo se ninguém estiver logado.
    fun getCurrentUser() = firebaseAuth.currentUser
}
