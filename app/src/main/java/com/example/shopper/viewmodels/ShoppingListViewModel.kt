package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ShoppingListDeserializer
import com.example.shopper.helpers.ShoppingListItemDeserializer
import com.example.shopper.models.ShoppingList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ShoppingListViewModel(private val userId: String) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private var shoppingDatabaseReference = database.child(Constants.FirebaseShoppingLists).child(userId)

    val shopperLiveData: LiveData<List<ShoppingList>> = Transformations.map(FirebaseQueryData(shoppingDatabaseReference), ShoppingListDeserializer())

    fun getShoppingListItem(key: String): LiveData<ShoppingList> {
        return Transformations.map(FirebaseQueryData(shoppingDatabaseReference.child(key)), ShoppingListItemDeserializer())
    }

    fun addShoppingList(shoppingList: ShoppingList) {
        val key = shoppingDatabaseReference.push().key as String
        shoppingDatabaseReference.child(key).setValue(shoppingList)
    }

    fun deleteShoppingList(shoppingList: ShoppingList) {
        val childUpdates = HashMap<String, Any?>()
        childUpdates["/${Constants.FirebaseShoppingLists}/$userId/${shoppingList.key}"] = null
        childUpdates["/${Constants.FirebaseSharedWith}/${shoppingList.key}/$userId"] = null

        if (userId == shoppingList.userId) {
            childUpdates["/${Constants.FirebaseShoppingItems}/${shoppingList.key}"] = null
        }

        database.updateChildren(childUpdates)
    }

    fun archiveShoppingList(shoppingList: ShoppingList) {
        val ref = database.child(Constants.FirebaseSharedWith).child(shoppingList.key!!)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(input: DataSnapshot) {
                val childUpdates = HashMap<String, Any?>()
                childUpdates["/${Constants.FirebaseShoppingLists}/$userId/${shoppingList.key}"] = null
                childUpdates["/${Constants.FirebaseArchive}/$userId/${shoppingList.key}"] = shoppingList.toMap()

                for (data in input.children) {
                    if (data.value is String) {
                        break
                    }

                    childUpdates["/${Constants.FirebaseShoppingLists}/${data.key}/${shoppingList.key}"] = null
                    childUpdates["/${Constants.FirebaseArchive}/${data.key}/${shoppingList.key}"] = shoppingList.toMap()
                }

                database.updateChildren(childUpdates)
            }
        })
    }
}