package com.example.shopper.services

import android.util.Log
import com.example.shopper.helpers.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
    * Called when message is received.
    *
    * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
    */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = data["title"]!!
        val message = data["message"]!!

        Log.e("Messaging Service", title)

        NotificationHelper.createNotification(this, title, message, true)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            FirebaseDatabase.getInstance().reference
                .child("profiles")
                .child(auth.currentUser!!.uid)
                .child("instanceId")
                .setValue(token)
        }
    }

}