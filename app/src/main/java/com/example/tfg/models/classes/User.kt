package com.example.tfg.models.classes

data class User(
    val id: String = "",
    val name: String = "",
    val dogs: MutableList<Dog> = mutableListOf(),
    val reminder : MutableList<Reminder> = mutableListOf()
){
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to this.id,
            "display_name" to this.name,
            "dogs" to dogs,
            "reminder" to reminder
        )
    }
}