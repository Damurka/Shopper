package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Profile


/**
 * ViewModel used to update the list_item_friend from the ShareAdapter
 */
class ProfileItemViewModel(profile: Profile) : ViewModel() {
    val name = ObservableField(profile.name)
    val email = ObservableField(profile.email)
    val image = ObservableField(profile.imageUrl)
}
