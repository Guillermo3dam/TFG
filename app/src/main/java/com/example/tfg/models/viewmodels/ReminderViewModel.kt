package com.example.tfg.models.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.classes.Reminder
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReminderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userEmail = auth.currentUser?.email ?: ""

    var state: MutableState<ReminderState> = mutableStateOf(ReminderState.Empty)
    var reminderList: MutableList<Reminder> = mutableListOf()

    fun getReminders() {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                getReminderData()
            }
        } else {
            state.value = ReminderState.Failure("Usuario no autenticado")
        }
    }

    private suspend fun getReminderData() {
        state.value = ReminderState.Loading
        try {
            val document = db.collection("users").document(userEmail).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                val firebaseReminders = user?.reminder ?: emptyList()
                if (firebaseReminders.isNotEmpty()) {
                    reminderList = firebaseReminders.toMutableList()
                    state.value = ReminderState.Success(reminderList)
                } else {
                    state.value = ReminderState.Empty
                }
            } else {
                state.value = ReminderState.Empty
            }
        } catch (e: Exception) {
            Log.e("ReminderViewModel", "Error obteniendo datos de usuario: ${e.message}")
            state.value = ReminderState.Failure("Error al obtener datos del usuario: ${e.message}")
        }
    }

    fun addReminderToCurrentUser(reminder: Reminder) {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            viewModelScope.launch {
                try {
                    val userDocumentRef = db.collection("users").document(userEmail)
                    userDocumentRef.update("reminder", FieldValue.arrayUnion(reminder)).await()
                    Log.d("ViewModel", "Recordatorio agregado correctamente al usuario $userEmail")
                    reminderList.add(reminder)
                    state.value = ReminderState.Success(reminderList)
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error al agregar el recordatorio: $e")
                    state.value = ReminderState.Failure("Error al agregar el recordatorio: ${e.message}")
                }
            }
        } else {
            Log.e("ViewModel", "No se pudo obtener el correo electrónico del usuario actual")
            state.value = ReminderState.Failure("Usuario no autenticado")
        }
    }
}
