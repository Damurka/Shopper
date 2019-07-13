package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.ShoppingList

class ShoppingListItemViewModel(shoppingList: ShoppingList): ViewModel() {

    val name = ObservableField(shoppingList.name)
    val owner = ObservableField(shoppingList.owner)

}