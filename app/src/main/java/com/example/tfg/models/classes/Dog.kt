package com.example.tfg.models.classes


data class Dog(
    val id: String = "",
    val name: String = "",
    val dates: MutableList<String> = mutableListOf(),
    val birthday: String = "",
    val male: Boolean = false,
    val castrated: Boolean = false,
    val ppp: Boolean = false
)

{
    fun addDate(date: String) {
        dates.add(date)
    }

    fun removeDate(date: String) {
        dates.remove(date)
    }

    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "id" to this.id,
            "name" to this.name,
            "dates" to if (dates.isNotEmpty()) dates else null,
            "birthday" to this.birthday,
            "male" to this.male,
            "castrated" to this.castrated,
            "ppp" to this.ppp
        )
    }
}