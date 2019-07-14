package com.example.shopper.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingList(
    var key: String = "",
    val name: String = "",
    val owner: String = "",
    val created: String = "",
    val modified: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "key" to key,
            "name" to name,
            "owner" to owner
        )
    }
}