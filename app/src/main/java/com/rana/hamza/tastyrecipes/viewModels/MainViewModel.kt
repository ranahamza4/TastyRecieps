package com.rana.hamza.tastyrecipes.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.rana.hamza.tastyrecipes.data.Repository
import com.rana.hamza.tastyrecipes.data.database.RecipeEntity
import com.rana.hamza.tastyrecipes.models.FoodRecipe
import com.rana.hamza.tastyrecipes.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE */
    lateinit var readRecipes: LiveData<List<RecipeEntity>>

    init {
        readRecipes()
    }

    private fun readRecipes(){
        viewModelScope.launch(Dispatchers.IO) {
            readRecipes=    repository.local.readDatabase().asLiveData()
        }
    }
    private fun insertRecipes(recipeEntity: RecipeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipeEntity)
        }
    }

    /** RETROFIT */
    val recipeResponse = MutableLiveData<NetworkResult<FoodRecipe>>()

    fun getRecipes(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        viewModelScope.launch {

            try {
                val a = hasInternetConnection()
                println(a.toString())
                val result = repository.getRecipes(a, queries)
                processResponse(result)
                val foodRecipe= recipeResponse.value!!.data
                if (foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error(null, "No Recipe Found")
            }

        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {

        val recipeEntity= RecipeEntity(foodRecipe)
        insertRecipes(recipeEntity)

    }

    private fun processResponse(result: Response<FoodRecipe>) {
        when {
            result.message().toString().contains("timeout") -> {
                recipeResponse.value = NetworkResult.Error(null, "Timeout")
            }
            result.code() == 402 -> {
                recipeResponse.value = NetworkResult.Error(null, "Limited Reached")
            }
            result.code() == 404 -> {
                recipeResponse.value = NetworkResult.Error(null, "No Internet")
            }
            result.body()!!.results.isEmpty() -> {
                recipeResponse.value = NetworkResult.Error(null, "No Recipe Found")
            }
            result.isSuccessful -> {
                recipeResponse.value = NetworkResult.Success(result.body()!!)
            }
            else -> {
                recipeResponse.value = NetworkResult.Error(null, "Something went wrong!")

            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val manager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val hasNetwork = manager.activeNetwork
        val capabilities = manager.getNetworkCapabilities(hasNetwork)

        return if (capabilities == null) {
            false
        } else {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false

            }
        }
    }


}