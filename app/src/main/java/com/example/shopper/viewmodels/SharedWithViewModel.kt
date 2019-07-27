package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.FriendsDeserializer
import com.example.shopper.models.Friend
import com.google.firebase.database.FirebaseDatabase

class SharedWithViewModel(listId: String): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private val sharedWithDatabaseReference = database.child(Constants.FirebaseSharedWith).child(listId)

    val sharedWithLiveData: LiveData<List<Friend>> = Transformations.map(FirebaseQueryData(sharedWithDatabaseReference), FriendsDeserializer())
}