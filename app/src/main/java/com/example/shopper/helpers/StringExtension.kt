package com.example.shopper.helpers

fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
    it.capitalize()
}