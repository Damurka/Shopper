package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Recipe


/**
 * ViewModel used to update the list_item_recipe from the RecipeAdapter
 */
class RecipeItemViewModel(recipe: Recipe): ViewModel() {

    val title = ObservableField(recipe.label)
    val imageUrl = ObservableField(recipe.image)
}