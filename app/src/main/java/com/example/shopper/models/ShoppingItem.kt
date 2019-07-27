package com.example.shopper.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingItem(
    val name: String = "",
    val owner: String = "",
    val bought: Boolean = false,
    val boughtBy: String = ""
) {

    @Exclude
    var key: String? = null
}