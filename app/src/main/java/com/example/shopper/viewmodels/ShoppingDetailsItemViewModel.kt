package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.ShoppingItem

class ShoppingDetailsItemViewModel(shoppingItem: ShoppingItem): ViewModel() {

    val name = ObservableField(shoppingItem.name)
    val isBought = ObservableField(shoppingItem.bought)
    val boughtBy = ObservableField(shoppingItem.boughtBy)

}