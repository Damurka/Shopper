package com.example.shopper.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friend(
    val email: String = "",
    val name: String = ""
) {

    @Exclude
    var key: String? = null

    @Exclude
    var shared: Boolean = false

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "name" to name
        )
    }

    override fun toString(): String {
        return "Friend(email = $email, name = $name, shared = $shared)"
    }
}