package com.example.tfg.models.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.Reminder
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime

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
                    // Ordena los recordatorios por fechas utilizando un comparador personalizado
                    val sortedReminders = firebaseReminders.sortedWith(compareBy { reminder ->
                        val (day, month, year) = reminder.date.split("/").map { it.toInt() }
                        val (hour, minute) = reminder.hour.split(":").map { it.toInt() }
                        LocalDateTime.of(year, month, day, hour, minute)
                    }).toMutableList()

                    state.value = ReminderState.Success(sortedReminders)
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


    fun deleteRemindersByDogId(dogId: String, callback: () -> Unit) {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                try {
                    val userDocumentRef = db.collection("users").document(userEmail)

                    val userDocumentSnapshot = userDocumentRef.get().await()
                    if (userDocumentSnapshot.exists()) {
                        val user = userDocumentSnapshot.toObject(User::class.java)
                        val remindersToDelete = user?.reminder?.filter { it.dogId == dogId } ?: emptyList()

                        remindersToDelete.forEach { reminder ->
                            userDocumentRef.update("reminder", FieldValue.arrayRemove(reminder)).await()
                        }

                        // Update local state
                        reminderList.removeAll(remindersToDelete)
                        state.value = if (reminderList.isNotEmpty()) {
                            ReminderState.Success(reminderList)
                        } else {
                            ReminderState.Empty
                        }
                        callback()
                    } else {
                        state.value = ReminderState.Failure("El documento del usuario no existe")
                        callback()
                    }
                } catch (e: Exception) {
                    state.value = ReminderState.Failure("Error al eliminar los recordatorios del perro: ${e.message}")
                    callback()
                }
            }
        } else {
            state.value = ReminderState.Failure("Usuario no autenticado")
            callback()
        }
    }


    fun deleteReminderFromCurrentUser(reminder: Reminder) {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            viewModelScope.launch {
                try {
                    val userDocumentRef = db.collection("users").document(userEmail)
                    userDocumentRef.update("reminder", FieldValue.arrayRemove(reminder)).await()
                    Log.d("ViewModel", "Recordatorio eliminado correctamente del usuario $userEmail")

                    // Actualizar la lista local de recordatorios y el estado
                    reminderList.remove(reminder)
                    getReminders()
                    state.value = if (reminderList.isNotEmpty()) {
                        ReminderState.Success(reminderList)

                    } else {
                        ReminderState.Empty
                    }
                } catch (e: Exception) {
                    Log.e("ViewModel", "Error al eliminar el recordatorio: $e")
                    state.value = ReminderState.Failure("Error al eliminar el recordatorio: ${e.message}")
                }
            }
        } else {
            Log.e("ViewModel", "No se pudo obtener el correo electrónico del usuario actual")
            state.value = ReminderState.Failure("Usuario no autenticado")
        }
    }



    fun deleteRemindersByDogId(dogId: String) {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            viewModelScope.launch {
                try {
                    val remindersToDelete = reminderList.filter { it.dogId == dogId }
                    val userDocumentRef = db.collection("users").document(userEmail)
                    remindersToDelete.forEach { reminder ->
                        userDocumentRef.update("reminder", FieldValue.arrayRemove(reminder)).await()
                    }
                    reminderList.removeAll(remindersToDelete)
                    state.value = if (reminderList.isNotEmpty()) {
                        ReminderState.Success(reminderList)
                    } else {
                        ReminderState.Empty
                    }
                } catch (e: Exception) {
                    state.value = ReminderState.Failure("Error al eliminar los recordatorios del perro: ${e.message}")
                }
            }
        } else {
            state.value = ReminderState.Failure("Usuario no autenticado")
        }
    }

}
