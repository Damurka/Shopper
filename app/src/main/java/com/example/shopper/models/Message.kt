package com.example.shopper.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Message (
    val title: String = "",
    val message: String = ""
) {

    @Exclude
    var key: String? = null

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "message" to message
        )
    }
}