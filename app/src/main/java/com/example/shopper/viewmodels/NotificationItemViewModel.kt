package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Message


/**
 * ViewModel used to update the list_item_notifications from the NotificationsAdapter
 */
class NotificationItemViewModel(message: Message): ViewModel() {

    val title = ObservableField(message.title)
    val message = ObservableField(message.message)
}