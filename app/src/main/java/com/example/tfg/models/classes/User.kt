package com.example.tfg.models.classes

data class User(
    val id: String = "",
    val name: String = "",
    val myDogs: MutableList<Dog> = mutableListOf()
){
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to this.id,
            "display_name" to this.name,
            "myDogs" to myDogs
        )
    }
}