package com.example.shopper.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ShoppingDetailDeserializer
import com.example.shopper.models.ShoppingItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ShoppingDetailsViewModel(listId: String): ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference

    private var shoppingDatabaseReference = database.child(Constants.FirebaseShoppingItems).child(listId)

    val shopperLiveData: LiveData<List<ShoppingItem>> = Transformations.map(FirebaseQueryData(shoppingDatabaseReference), ShoppingDetailDeserializer())

    fun addShoppingListItem(shoppingItem: ShoppingItem) {
        val key = shoppingDatabaseReference.push().key as String
        shoppingDatabaseReference.child(key).setValue(shoppingItem)
    }

    fun buyItem(shoppingItem: ShoppingItem) {
        shoppingDatabaseReference.child(shoppingItem.key!!).setValue(shoppingItem)
    }

    fun deleteShoppingItem(key: String) {
        shoppingDatabaseReference.child(key).removeValue()
    }

}