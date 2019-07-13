package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ShoppingListDeserializer
import com.example.shopper.models.ShoppingList
import com.google.firebase.database.FirebaseDatabase


class ShoppingListViewModel(userId: String) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private var shoppingDatabaseReference = database.child(Constants.FirebaseShoppingLists).child(userId)

    val shopperLiveData: LiveData<List<ShoppingList>> = Transformations.map(FirebaseQueryData(shoppingDatabaseReference), ShoppingListDeserializer())

    fun addShoppingList(shoppingList: ShoppingList) {
        val key = shoppingDatabaseReference.push().key as String
        shoppingList.key = key
        shoppingDatabaseReference.child(key).setValue(shoppingList)
    }

    fun deleteShoppingList(key: String) {
        shoppingDatabaseReference.child(key).removeValue()
    }
}