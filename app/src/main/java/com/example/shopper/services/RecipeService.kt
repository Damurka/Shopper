package com.example.shopper.services

import com.example.shopper.models.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RecipeService {

    @GET("search")
    fun searchRecipe(@Query("q") query: String, @Query("app_id") appId: String, @Query("app_key") appKey: String): Call<RecipeResponse>

}