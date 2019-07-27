package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Profile

class ProfileItemViewModel(profile: Profile) : ViewModel() {
    val name = ObservableField(profile.name)
    val email = ObservableField(profile.email)
    val image = ObservableField(profile.imageUrl)
}
