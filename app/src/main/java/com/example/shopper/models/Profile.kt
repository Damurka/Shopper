package com.example.shopper.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Profile(
    var email: String = "",
    var name: String = "",
    var phone: String? = "",
    var imageUrl: String? = "") {

    @Exclude
    var key: String? = null

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "name" to name,
            "phone" to phone,
            "imageUrl" to imageUrl
        )
    }
}