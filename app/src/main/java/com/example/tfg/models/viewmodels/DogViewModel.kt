package com.example.tfg.models.viewmodels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tfg.models.classes.Dog
import com.example.tfg.models.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class DogViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    val userEmail = auth.currentUser?.email
    private val db = FirebaseFirestore.getInstance()

    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogs: StateFlow<List<Dog>> = _dogs


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

        // Obtener una referencia al documento del usuario en Firestore
        val userDocumentRef =
            FirebaseFirestore.getInstance().collection("users").document(userEmail.toString())

        // Agregar el nuevo perro directamente a la lista de perros del usuario en Firestore
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val currentDogsList =
                    documentSnapshot.toObject(User::class.java)?.myDogs?: mutableListOf()

                // Agregar el nuevo perro a la lista actual de perros
                currentDogsList.add(dog)

                // Actualizar la lista de perros en Firestore con la clave correcta
                userDocumentRef.update("myDogs", currentDogsList)
                    .addOnSuccessListener {
                        Log.d(
                            ContentValues.TAG,
                            "Perro añadido correctamente a la lista de perros del usuario"
                        )
                        // Aquí puedes realizar acciones adicionales después de agregar el perro
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            ContentValues.TAG,
                            "Error al añadir el perro a la lista de perros del usuario",
                            e
                        )
                    }
            } else {
                Log.d(ContentValues.TAG, "El documento del usuario no existe en Firestore")
            }
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error al obtener el documento del usuario", e)
        }
    }


    fun loadDogs() {

        if (userEmail != null) {
            db.collection("users")
                .document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        user?.myDogs?.let { dogs ->
                            // Iterar sobre la lista de perros y mostrar por consola sus atributos
                            for (dog in dogs) {
                                Log.d(TAG, "Dog ID: ${dog.id}")
                                Log.d(TAG, "Dog Name: ${dog.name}")
                                Log.d(TAG, "Dog Birthday: ${dog.birthday}")
                                Log.d(TAG, "Dog Male: ${dog.male}")
                                Log.d(TAG, "Dog Castrated: ${dog.castrated}")
                                Log.d(TAG, "Dog PPP: ${dog.ppp}")
                                Log.d(TAG, "-------------------------")
                            }
                        }
                    } else {
                        // El documento del usuario no existe
                        println("El documento del usuario no existe.")
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar el error en caso de que falle la carga de los perros del usuario
                    println("Error al cargar los perros del usuario: $exception")
                }
        } else {
            // No hay un usuario actualmente autenticado
            println("No hay un usuario actualmente autenticado.")
        }
    }
}


