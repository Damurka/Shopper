package com.example.shopper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ShareViewModelFactory (private val userId: String) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ShareViewModel(userId) as T

}