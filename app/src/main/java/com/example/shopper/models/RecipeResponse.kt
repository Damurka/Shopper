package com.example.shopper.models

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("hits")
    var recipes: List<RecipeCover>
)