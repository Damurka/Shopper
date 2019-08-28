package com.example.shopper.helpers

/**
 * Capitalize every first letter in a sentence
 */
fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
    it.capitalize()
}