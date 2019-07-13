package com.example.shopper.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*


class FirebaseListQueryData(private val query: Query) : LiveData<DataSnapshot>() {

    private val listener = MyChildEventListener()

    constructor(ref: DatabaseReference) : this(ref as Query)

    override fun onActive() {
        query.addChildEventListener(listener)
    }

    override fun onInactive() {
        query.removeEventListener(listener)
    }

    private inner class MyChildEventListener : ChildEventListener {

        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            value = dataSnapshot
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("MyValueEventListener", "Can't listen to query $query", databaseError.toException())
        }

    }
}