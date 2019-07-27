package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.FriendsDeserializer
import com.example.shopper.models.Friend
import com.example.shopper.models.ShoppingList
import com.google.firebase.database.FirebaseDatabase

class ShareViewModel(private val userId: String) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private val friendsDatabaseReference = database.child(Constants.FirebaseFriends).child(userId)

    val friendsLiveData: LiveData<List<Friend>> = Transformations.map(FirebaseQueryData(friendsDatabaseReference), FriendsDeserializer())

    fun addFriend(key: String, friend: Friend) {
        friendsDatabaseReference.child(key).setValue(friend)
    }

    fun removeFriend(friend: Friend, shoppingLists: List<ShoppingList>) {
        val childUpdates = HashMap<String, Any?>()
        childUpdates["/${Constants.FirebaseFriends}/$userId/${friend.key}"] = null
        for (shoppingList in shoppingLists) {
            childUpdates["/${Constants.FirebaseSharedWith}/${shoppingList.key}/${friend.key}"] = null
            childUpdates["/${Constants.FirebaseShoppingLists}/${friend.key}/${shoppingList.key}"] = null
        }

        database.updateChildren(childUpdates)
    }

    fun shareWith(friend: Friend, shoppingList: ShoppingList) {
        val childUpdates = HashMap<String, Any>()
        childUpdates["/${Constants.FirebaseSharedWith}/${shoppingList.key}/${friend.key}"] = friend.toMap()
        childUpdates["/${Constants.FirebaseShoppingLists}/${friend.key}/${shoppingList.key}"] = shoppingList.toMap()

        database.updateChildren(childUpdates)
    }
}