package com.example.tfg.models.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class UserViewModel : ViewModel() {

    val user = mutableStateOf(User())
    var state: MutableState<UserState> = mutableStateOf(UserState.Empty)



    fun getUser() {
        viewModelScope.launch {
            getUserData()
        }
    }
    private fun getUserData() {
        val auth: FirebaseAuth = Firebase.auth
        val userEmail = auth.currentUser?.email.toString()

        val currentuser = auth.currentUser
        state.value = UserState.Loading


        FirebaseFirestore.getInstance().collection("users").document(userEmail)
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
}