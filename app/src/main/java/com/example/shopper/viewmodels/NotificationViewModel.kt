package com.example.shopper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.MessageDeserializer
import com.example.shopper.helpers.NotificationHelper
import com.example.shopper.models.Message
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class NotificationViewModel(private val context: Context, userId: String) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private val notificationDatabaseReference = database.child(Constants.FirebaseNotifications)

    private val myNotificationDatabaseReference = database.child(Constants.FirebaseNotifications).child(userId)

    val notificationLiveData: LiveData<List<Message>> = Transformations.map(FirebaseQueryData(myNotificationDatabaseReference), MessageDeserializer())

    init {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                val message = dataSnapshot.getValue(Message::class.java)

                NotificationHelper.createNotification(context, message!!.title, message.message, true)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())

            }
        }
        myNotificationDatabaseReference.addChildEventListener(childEventListener)
    }

    fun addMessage(userId: String, message: Message) {
        val ref = notificationDatabaseReference.child(userId)
        val key = ref.push().key as String
        ref.child(key).setValue(message)
    }

    companion object {
        const val TAG = "NotificationViewModel"
    }

}