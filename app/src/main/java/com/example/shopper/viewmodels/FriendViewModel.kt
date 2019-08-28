package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Friend


/**
 * ViewModel used to update the list_item_friend from the ShareAdapter
 */
class FriendViewModel(friend: Friend, isOwner: Boolean) : ViewModel() {
    val name = ObservableField(friend.name)
    val email = ObservableField(friend.email)
    val shared = ObservableField(friend.shared)
    val isCreator = ObservableField(isOwner)
}
