package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Message

class NotificationItemViewModel(message: Message): ViewModel() {

    val title = ObservableField(message.title)
    val message = ObservableField(message.message)
}