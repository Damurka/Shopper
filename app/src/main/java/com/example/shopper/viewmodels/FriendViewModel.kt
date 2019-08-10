package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Friend


class FriendViewModel(friend: Friend, isOwner: Boolean) : ViewModel() {
    val name = ObservableField(friend.name)
    val email = ObservableField(friend.email)
    val shared = ObservableField(friend.shared)
    val isCreator = ObservableField(isOwner)
}
