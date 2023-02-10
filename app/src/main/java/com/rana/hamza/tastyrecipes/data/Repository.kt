package com.rana.hamza.tastyrecipes.data

import com.rana.hamza.tastyrecipes.models.FoodRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    private val remote = remoteDataSource
     val local= localDataSource


    suspend fun getRecipes(
        hasNetwork: Boolean,
        queries: Map<String, String>
    ): Response<FoodRecipe> {


        return if (hasNetwork) {
            withContext(Dispatchers.IO){
                remote.getRecipes(queries)
            }

        } else {
            Response.error(404, "No Internet Connection".toResponseBody(null))
        }
    }
}