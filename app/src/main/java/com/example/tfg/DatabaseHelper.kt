package com.example.tfg

import com.google.firebase.database.*

class DatabaseHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    fun addUser(username: String, password: String, callback: (Boolean) -> Unit) {
        val userId = database.push().key
        if (userId != null) {
            val user = User(userId, username, password)
            database.child(userId).setValue(user)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
        } else {
            callback(false)
        }
    }

    fun getUser(username: String, password: String, callback: (Boolean) -> Unit) {
        database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.password == password) {
                            callback(true)
                            return
                        }
                    }
                }
                callback(false)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    data class User(
        var id: String? = "",
        var username: String? = "",
        var password: String? = ""
    )
}
