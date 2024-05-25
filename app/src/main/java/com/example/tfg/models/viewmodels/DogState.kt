package com.example.tfg.models.viewmodels

import com.example.tfg.models.classes.Dog

sealed class DogState {
    class Success(val data: MutableList<Dog>) : DogState()
    class Failure(val message: String) : DogState()
    object Loading : DogState()
    object Empty : DogState()

}