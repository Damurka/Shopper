package com.example.shopper.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*

class FirebaseQueryData(private val query: Query) : LiveData<DataSnapshot>() {

    private val listener = MyValueEventListener()

    constructor(ref: DatabaseReference) : this(ref as Query)

    override fun onActive() {
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("MyValueEventListener", "Can't listen to query $query", databaseError.toException())
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

    }
}