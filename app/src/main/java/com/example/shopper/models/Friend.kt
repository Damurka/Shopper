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
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "name" to name
        )
    }
}