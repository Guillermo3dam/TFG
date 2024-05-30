package com.example.tfg.models.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.UUID

class DogViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    val userEmail = auth.currentUser?.email.toString()
    private val db = FirebaseFirestore.getInstance()

    var state: MutableState<DogState> = mutableStateOf(DogState.Empty)
    var doglist: MutableList<Dog> = mutableListOf()
    var selectedDog: MutableState<Dog?> = mutableStateOf(null)




    fun updateDog(id: String, name: String, birthday: String, isMale: Boolean, isNeutered: Boolean, isPPP: Boolean) {
        val userDocumentRef = db.collection("users").document(userEmail)

        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)
                user?.dogs?.let { dogs ->
                    val updatedDogsList = dogs.map { dog ->
                        if (dog.id == id) {
                            dog.copy(
                                name = name,
                                birthday = birthday,
                                male = isMale,
                                castrated = isNeutered,
                                ppp = isPPP
                            )
                        } else {
                            dog
                        }
                    }
                    userDocumentRef.update("dogs", updatedDogsList)
                        .addOnSuccessListener {
                            Log.d("DogViewModel", "Dog updated successfully")
                            // Optionally, you can call getDogs() to refresh the dog list in the ViewModel
                            getDogs()
                        }
                        .addOnFailureListener { e ->
                            Log.w("DogViewModel", "Error updating dog", e)
                        }
                }
            } else {
                Log.d(ContentValues.TAG, "El documento del usuario no existe.")
            }
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error al obtener el documento del usuario", e)
        }
    }







    fun getDogById(dogId: String, callback: (Dog?) -> Unit) {
        if (auth.currentUser != null) {
            val userDocumentRef = db.collection("users").document(userEmail)

            userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.dogs?.let { dogs ->
                        val dog = dogs.find { it.id == dogId }
                        if (dog != null) {
                            callback(dog)
                            Log.d("DogViewModel", "Dog found: ${dog.name}")
                        } else {
                            callback(null)
                            Log.d("DogViewModel", "Dog not found with ID: $dogId")
                        }
                    }
                } else {
                    Log.d(ContentValues.TAG, "El documento del usuario no existe.")
                    callback(null)
                }
            }.addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error al obtener el documento del usuario", e)
                callback(null)
            }
        } else {
            callback(null)
        }
    }




    fun deleteDog(dogId: String, reminderViewModel: ReminderViewModel) {
        if (auth.currentUser != null) {
            val userDocumentRef = db.collection("users").document(userEmail)

            userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    user?.dogs?.let { dogs ->
                        val updatedDogsList = dogs.filter { it.id != dogId }
                        userDocumentRef.update("dogs", updatedDogsList)
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "Perro eliminado correctamente")

                                // Eliminar los recordatorios asociados al perro
                                reminderViewModel.deleteRemindersByDogId(dogId) {
                                    getDogs()  // Actualizar la lista de perros
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error al eliminar el perro", e)
                                state.value = DogState.Failure("Error al eliminar el perro: ${e.message}")
                            }
                    }
                } else {
                    Log.d(ContentValues.TAG, "El documento del usuario no existe.")
                    state.value = DogState.Empty
                }
            }.addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error al obtener el documento del usuario", e)
                state.value = DogState.Failure("Error al obtener el documento del usuario: ${e.message}")
            }
        } else {
            state.value = DogState.Failure("Usuario no autenticado")
        }
    }



    fun getDogs() {
        if (auth.currentUser != null) {
            state.value = DogState.Loading
            viewModelScope.launch {
                getDogData()
            }
        } else {
            state.value = DogState.Failure("Usuario no autenticado")
        }
    }

    private fun getDogData() {
        db.collection("users").document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    val firestoreDog = mutableListOf<Dog>()
                    user?.dogs?.let { dogs ->
                        for (dog in dogs) {
                            Log.d("DogViewModel", "Dog ID: ${dog.id}")
                            firestoreDog.add(dog)
                        }
                        doglist = firestoreDog
                        state.value = if (firestoreDog.isEmpty()) DogState.Empty else DogState.Success(doglist)
                    } ?: run {
                        state.value = DogState.Empty
                    }
                } else {
                    Log.d("DogViewModel", "El documento del usuario no existe.")
                    state.value = DogState.Empty
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DogViewModel", "Error obteniendo datos de usuario: ${exception.message}")
                state.value = DogState.Failure("Error al obtener datos del usuario: ${exception.message}")
            }
    }




fun addDog(
        name: String,
        birthday: String,
        isMale: Boolean,
        isNeutered: Boolean,
        isPPP: Boolean
    ) {
        val dog = Dog(
            id = UUID.randomUUID().toString(),
            name = name,
            birthday = birthday,
            male = isMale,
            castrated = isNeutered,
            ppp = isPPP
        )
        Log.d(ContentValues.TAG, "id del perro ${dog.id}")

        val userDocumentRef = db.collection("users").document(userEmail)


        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val currentDogsList = documentSnapshot.toObject(User::class.java)?.dogs ?: mutableListOf()
                currentDogsList.add(dog)
                userDocumentRef.update("dogs", currentDogsList)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Perro añadido correctamente a la lista de perros del usuario")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error al añadir el perro a la lista de perros del usuario", e)
                    }
            } else {
                Log.d(ContentValues.TAG, "El documento del usuario no existe en Firestore")
            }
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error al obtener el documento del usuario", e)
        }
    }
}