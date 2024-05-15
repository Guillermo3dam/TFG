package com.example.tfg.models.viewmodels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg.models.classes.User
import com.example.tfg.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel : ViewModel(){
    private val auth : FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(email: String, password : String, home: ()-> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {authResult->
                    Log.d(TAG, "singInWithEmailAndPassword Logueado!!!: ${authResult.toString()}")
                    home()
                }
                .addOnFailureListener{ex->
                    // añadir código mensaje error


                    // Tienes acceso a la excepción
                    Log.d(TAG, "singInWithEmailAndPassword Falló!!!: ${ex.localizedMessage}")
                    //errorLogueo()
                }

        }catch (ex: Exception){
            Log.d(TAG, "singInWithEmailAndPassword: ${ex.message}")
        }
    }

    fun createUsersWithEmailAndPassword(
        email:String,
        password: String,
        home:() -> Unit
    ){
        if(_loading.value == false){
            _loading.value = true

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }else{
                        Log.d(TAG, "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val userEmail = auth.currentUser?.email

        // uso el data class
        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            email =  userEmail.toString()
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d(TAG, "Creado ${it.id}")
            }.addOnFailureListener{
                Log.d(TAG, "Ocurrio un error ${it}")
            }
    }
}