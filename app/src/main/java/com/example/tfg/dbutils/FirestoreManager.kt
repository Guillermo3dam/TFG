package com.example.tfg.dbutils

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
    var userId = auth.currentUser?.uid


    suspend fun updateUser(user: User) {
        val userRef = user.id?.let { firestore.collection("users").document(it) }
        userRef?.set(user)?.await()
    }

    suspend fun deleteUser(email : String) {
        val userRef = firestore.collection("users").document(email)
        userRef.delete().await()
    }
}