package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.ShoppingList


/**
 * ViewModel used to update the list_item_shopping from the ShoppingAdapter
 */
class ShoppingListItemViewModel(shoppingList: ShoppingList, userId: String): ViewModel() {

    val name = ObservableField(shoppingList.name)
    val owner = ObservableField(shoppingList.owner)
    val isCreator = ObservableField(shoppingList.userId == userId)

}