package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ShoppingListDeserializer
import com.example.shopper.helpers.ShoppingListItemDeserializer
import com.example.shopper.models.ShoppingItem
import com.example.shopper.models.ShoppingList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ShoppingListViewModel(private val userId: String) : ViewModel() {

    /**
     * Firebase Database reference
     */
    private val database = FirebaseDatabase.getInstance().reference

    /**
     * Firebase Database shopping-list reference
     */
    private var shoppingDatabaseReference = database.child(Constants.FirebaseShoppingLists).child(userId)

    /**
     * Retrieves the shopping lists ans LiveData
     */
    val shopperLiveData: LiveData<List<ShoppingList>> = Transformations.map(FirebaseQueryData(shoppingDatabaseReference), ShoppingListDeserializer())

    /**
     * Gets a particular shopping list as LiveData
     */
    fun getShoppingListItem(key: String): LiveData<ShoppingList> {
        return Transformations.map(FirebaseQueryData(shoppingDatabaseReference.child(key)), ShoppingListItemDeserializer())
    }

    /**
     * Adds a shopping List
     */
    fun addShoppingList(shoppingList: ShoppingList) {
        val key = shoppingDatabaseReference.push().key as String
        shoppingDatabaseReference.child(key).setValue(shoppingList)
    }

    /**
     * Adds Items to a shopping list
     */
    fun addShoppingList(shoppingList: ShoppingList, shoppingItems: List<ShoppingItem>) {
        val childUpdates = HashMap<String, Any?>()
        val key = shoppingDatabaseReference.push().key as String
        childUpdates["/${Constants.FirebaseShoppingLists}/$userId/$key"] = shoppingList.toMap()

        val ref = database.child(Constants.FirebaseShoppingItems).child(key)
        for (shoppingItem in shoppingItems) {
            val itemKey = ref.push().key
            childUpdates["/${Constants.FirebaseShoppingItems}/$key/$itemKey"] = shoppingItem.toMap()
        }

        database.updateChildren(childUpdates)
    }

    /**
     * Deletes a shopping list
     */
    fun deleteShoppingList(shoppingList: ShoppingList) {
        val childUpdates = HashMap<String, Any?>()
        childUpdates["/${Constants.FirebaseShoppingLists}/$userId/${shoppingList.key}"] = null
        childUpdates["/${Constants.FirebaseSharedWith}/${shoppingList.key}/$userId"] = null

        if (userId == shoppingList.userId) {
            childUpdates["/${Constants.FirebaseShoppingItems}/${shoppingList.key}"] = null
        }

        database.updateChildren(childUpdates)
    }

    /**
     * Archives a shopping list
     */
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