package com.example.tfg.dbutils

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class FirestoreManager : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val auth : FirebaseAuth = Firebase.auth





    suspend fun deleteUser(email: String) {
        val userRef = firestore.collection("users").document(email)
        try {
            userRef.delete().await()
            Log.d("firesotremanager", "usuario borrado.")
        } catch (e: Exception) {
            Log.w("firesotremanager", "error al borrar el usuario: ${e.message}")
            throw e
        }
    }
}