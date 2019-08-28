package com.example.shopper.helpers

import com.google.firebase.database.FirebaseDatabase

object Constants {
    const val FirebaseProfiles = "profiles"
    const val FirebaseShoppingLists = "shopping-lists"
    const val FirebaseShoppingItems = "shopping-items"
    const val FirebaseFriends = "shopping-friends"
    const val FirebaseSharedWith = "shopping-shared-with"
    const val FirebaseArchive = "shopping-archives"
    const val FirebaseNotifications = "notifications"

    val database = FirebaseDatabase.getInstance().reference
}