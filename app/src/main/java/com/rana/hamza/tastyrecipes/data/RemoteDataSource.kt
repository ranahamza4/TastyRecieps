package com.rana.hamza.tastyrecipes.data

import com.rana.hamza.tastyrecipes.data.network.FoodRecipesApi
import com.rana.hamza.tastyrecipes.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        foodRecipesApi.getRecipes(queries)
}