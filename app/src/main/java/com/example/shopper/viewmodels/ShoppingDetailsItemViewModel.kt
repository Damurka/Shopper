package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.ShoppingItem


/**
 * ViewModel used to update the list_item_shopping_item from the ShoppingDetailAdapter
 */
class ShoppingDetailsItemViewModel(shoppingItem: ShoppingItem): ViewModel() {

    val name = ObservableField(shoppingItem.name)
    val isBought = ObservableField(shoppingItem.bought)
    val boughtBy = ObservableField(shoppingItem.boughtBy)

}