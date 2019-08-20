package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.MessageDeserializer
import com.example.shopper.models.Message
import com.google.firebase.database.FirebaseDatabase


class NotificationViewModel(userId: String) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private val myNotificationDatabaseReference = database.child(Constants.FirebaseNotifications).child(userId)

    val notificationLiveData: LiveData<List<Message>> = Transformations.map(FirebaseQueryData(myNotificationDatabaseReference), MessageDeserializer())

    fun addMessage(message: Message, friendId: String) {
        val ref = database.child(Constants.FirebaseNotifications).child(friendId)
        val key = ref.push().key as String
        ref.child(key).setValue(message)
    }

}