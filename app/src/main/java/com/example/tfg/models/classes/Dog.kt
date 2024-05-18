package com.example.tfg.models.classes

data class Dog(
    val userId : String,
    val name : String,
    val dates: MutableList<String>,
    val birthday : String,
    val male : Boolean,
    val castrated : Boolean,
    val size : String,
    val behavior : String,
    val ppp : Boolean
    ){
    fun toMap(): MutableMap<String,Any> {
        return mutableMapOf(
            "user_id" to this.userId,
            "name" to this.name,
            "dates" to this.dates,
            "birthday" to this.birthday,
            "male" to this.male,
            "castrated" to this.castrated,
            "size" to this.size,
            "behavior" to this.behavior,
            "ppp" to this.ppp,
        )
    }
}