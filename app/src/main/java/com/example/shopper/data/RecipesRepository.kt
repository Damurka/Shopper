package com.example.shopper.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.shopper.models.RecipeCover
import com.example.shopper.models.RecipeResponse
import com.example.shopper.services.RecipeService
import com.example.shopper.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipesRepository private constructor(){

    private val recipeService = ServiceBuilder.buildService(RecipeService::class.java)

    fun getRecipes(query: String) : MutableLiveData<List<RecipeCover>> {
        val recipeData = MutableLiveData<List<RecipeCover>>()

        recipeService.searchRecipe(query, "e6f5f143", "3782c50113ad97726f12a0f702e800a3")
            .enqueue(object: Callback<RecipeResponse> {
                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                    Log.e("CategoryFragment", t.message)
                    recipeData.value = null
                }

                override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                    if (response.isSuccessful) {
                        recipeData.value = response.body()?.recipes
                    }
                }

            })

        return recipeData
    }

    companion object {
        @Volatile private var instance: RecipesRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RecipesRepository().also { instance = it }
            }
    }
}