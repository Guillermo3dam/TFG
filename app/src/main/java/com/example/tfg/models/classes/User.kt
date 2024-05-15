package com.example.tfg.models.classes

data class User(
    val email: String,
    val userId: String,
    val displayName: String,
    val avatarUrl: String
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "email" to this.email,
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "avatar_url" to this.avatarUrl
        )
    }
}
