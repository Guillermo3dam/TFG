package com.example.tfg.models.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel : ViewModel(){
    private val auth : FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit, onError: (String) -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        Log.d(TAG, "signInWithEmailAndPassword Logueado!!!: ${authResult.toString()}")
                        home()
                    }
                    .addOnFailureListener { ex ->
                        val errorMessage = ex.message ?: "Error desconocido"
                        Log.d(TAG, "signInWithEmailAndPassword Falló!!!: ${errorMessage}")
                        onError("Usuario o contraseña incorrectos.")
                    }
            } catch (ex: Exception) {
                Log.d(TAG, "signInWithEmailAndPassword: ${ex.message}")
                onError("Error al iniciar sesión.")
            }
        }

    fun createUsersWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName =
                            task.result?.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }
                    _loading.value = false
                }
                .addOnFailureListener { exception ->
                    val errorMessage = exception.message ?: "Error desconocido"
                    Log.d(TAG, "createUserWithEmailAndPassword error: $errorMessage")
                    onError("La direción de email ya esta en uso por otra cuenta")
                    _loading.value = false
                }
        }
    }


    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val userEmail = auth.currentUser?.email

        val user = User(
            id = userId.toString(),
            name = displayName.toString()
        ).toMap()
        FirebaseFirestore.getInstance().collection("users").document(userEmail.toString())
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, " usuario creado en la base de datos")
            }.addOnFailureListener{
                Log.d(TAG, "Ocurrio un error ${it}")
            }
    }

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
}