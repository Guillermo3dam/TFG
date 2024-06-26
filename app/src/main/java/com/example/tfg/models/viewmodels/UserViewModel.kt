package com.example.tfg.models.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class UserViewModel : ViewModel() {

    val user = mutableStateOf(User())
    var state: MutableState<UserState> = mutableStateOf(UserState.Empty)

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")


    fun getUser() {
        viewModelScope.launch {
            getUserData()
        }
    }

    private fun getUserData() {
        val auth: FirebaseAuth = Firebase.auth
        val userEmail = auth.currentUser?.email.toString()

        state.value = UserState.Loading


        db.collection("users").document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                val user1 = User(
                    id = document.get("user_id") as String,
                    name = document.get("display_name") as String
                )
                user.value = user1
                state.value = UserState.Success(user.value)
            }
            .addOnFailureListener { exception ->
                Log.d("UserData", "Error obteniendo datos de usuario: ${exception.message}")
                // Maneja el error según sea necesario
            }
    }


    suspend fun checkIfEmailExists(email: String): Boolean {
        return try {
            val documentSnapshot = usersCollection.document(email).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            // Manejar cualquier error que ocurra durante la consulta
            false
        }
    }

    // Función para enviar correo electrónico de restablecimiento de contraseña
    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkIfEmailExists(email)) {
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onError()
                        }
                    }
            } else {
                onError()
            }
        }
    }
}
