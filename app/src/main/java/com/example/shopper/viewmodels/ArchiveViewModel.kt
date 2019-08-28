package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ShoppingListDeserializer
import com.example.shopper.helpers.ShoppingListItemDeserializer
import com.example.shopper.models.ShoppingList
import com.google.firebase.database.FirebaseDatabase


/**
 * Holds the data on the archived shopping list
 */
class ArchiveViewModel(private val userId: String) : ViewModel() {

    /**
     * Firebase Database reference
     */
    private val database = FirebaseDatabase.getInstance().reference

    /**
     * Archive database reference
     */
    private var archiveDatabaseReference = database.child(Constants.FirebaseArchive).child(userId)

    /**
     * Archived list Transformed to observable LiveData
     */
    val archivesLiveData: LiveData<List<ShoppingList>> = Transformations.map(FirebaseQueryData(archiveDatabaseReference), ShoppingListDeserializer())

    /**
     * Gets a single Archived List as LiveData
     */
    fun getArchiveItem(key: String): LiveData<ShoppingList> {
        return Transformations.map(FirebaseQueryData(archiveDatabaseReference.child(key)), ShoppingListItemDeserializer())
    }

    /**
     * Deletes a single Archived  List
     */
    fun deleteShoppingList(shoppingList: ShoppingList, email: String) {
        val childUpdates = HashMap<String, Any?>()
        childUpdates["/${Constants.FirebaseArchive}/$userId/${shoppingList.key}"] = null
        childUpdates["/${Constants.FirebaseSharedWith}/${shoppingList.key}/$userId"] = null

        if (email == shoppingList.owner) {
            childUpdates["/${Constants.FirebaseShoppingItems}/${shoppingList.key}"] = null
        }

        database.updateChildren(childUpdates)
    }
}