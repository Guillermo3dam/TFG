package com.example.tfg.models.viewmodels

import com.example.tfg.models.classes.User


sealed class UserState {
    class Success(val data: User) : UserState()
    class Failure(val message: String) : UserState()
    object Loading : UserState()
    object Empty : UserState()

}