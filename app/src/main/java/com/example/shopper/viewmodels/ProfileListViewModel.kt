package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ProfileListDeserializer
import com.example.shopper.models.Profile
import com.google.firebase.database.FirebaseDatabase


class ProfileListViewModel : ViewModel() {

    /**
     * Firebase database reference
     */
    private val database = FirebaseDatabase.getInstance().reference

    /**
     * Firebase datbase Profile reference
     */
    val profileDatabaseReference = database.child(Constants.FirebaseProfiles)

    /**
     * Retrieves the users list as LiveData
     */
    val profileListLiveData: LiveData<List<Profile>> = Transformations.map(FirebaseQueryData(profileDatabaseReference), ProfileListDeserializer())

}