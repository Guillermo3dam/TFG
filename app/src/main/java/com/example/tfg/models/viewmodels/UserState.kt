package com.example.tfg.models.viewmodels

import com.example.tfg.models.classes.User


sealed class UserState { // manejo de los estados que se puede encontrar los datos
    class Success(val data: User) : UserState()
    class Failure(val message: String) : UserState()
    object Loading : UserState()
    object Empty : UserState()

}