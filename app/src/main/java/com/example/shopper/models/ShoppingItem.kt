package com.example.shopper.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShoppingItem(
    var key: String = "",
    val name: String = "",
    val owner: String = "",
    val bought: Boolean = false,
    val boughtBy: String = ""
)