package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ShoppingDetailDeserializer
import com.example.shopper.models.ShoppingItem
import com.google.firebase.database.FirebaseDatabase


class ShoppingDetailsViewModel(listId: String): ViewModel() {
    /**
     * Firebase Database reference
     */
    private val database = FirebaseDatabase.getInstance().reference

    /**
     * Firebase Database shopping-items reference
     */
    private var shoppingDatabaseReference = database.child(Constants.FirebaseShoppingItems).child(listId)

    /**
     * Retrieves a shopping list items as LiveData
     */
    val shopperLiveData: LiveData<List<ShoppingItem>> = Transformations.map(FirebaseQueryData(shoppingDatabaseReference), ShoppingDetailDeserializer())

    /**
     * Add Shopping List Item
     */
    fun addShoppingListItem(shoppingItem: ShoppingItem) {
        val key = shoppingDatabaseReference.push().key as String
        shoppingDatabaseReference.child(key).setValue(shoppingItem)
    }

    /**
     * Changes when an item is cliked as bought
     */
    fun buyItem(shoppingItem: ShoppingItem) {
        shoppingDatabaseReference.child(shoppingItem.key!!).setValue(shoppingItem)
    }

    /**
     * Delete Shopping List Item
     */
    fun deleteShoppingItem(key: String) {
        shoppingDatabaseReference.child(key).removeValue()
    }

}