package com.example.tfg.models.classes


data class Dog(
    val id: String = "",
    val name: String = "",
    val birthday: String = "",
    val male: Boolean = false,
    val castrated: Boolean = false,
    val ppp: Boolean = false
)

{

    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "id" to this.id,
            "name" to this.name,
            "birthday" to this.birthday,
            "male" to this.male,
            "castrated" to this.castrated,
            "ppp" to this.ppp
        )
    }
}