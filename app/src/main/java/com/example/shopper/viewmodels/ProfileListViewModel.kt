package com.example.shopper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ProfileListDeserializer
import com.example.shopper.models.Profile


class ProfileListViewModel : ViewModel() {

    val profileListLiveData: LiveData<List<Profile>> = Transformations.map(FirebaseQueryData(Constants.profileDatabaseReference), ProfileListDeserializer())

}