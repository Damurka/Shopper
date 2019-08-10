package com.example.shopper.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.shopper.models.Recipe

class RecipeItemViewModel(recipe: Recipe): ViewModel() {

    val title = ObservableField(recipe.label)
    val imageUrl = ObservableField(recipe.image)
}