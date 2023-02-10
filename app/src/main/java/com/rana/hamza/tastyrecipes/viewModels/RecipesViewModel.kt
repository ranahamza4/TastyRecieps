package com.rana.hamza.tastyrecipes.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rana.hamza.tastyrecipes.data.DataStoreRepo
import com.rana.hamza.tastyrecipes.util.Constants.Companion.API_KEY
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_QUERY_NUMBER
import com.rana.hamza.tastyrecipes.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.rana.hamza.tastyrecipes.util.Constants.Companion.QUERY_API_KEY
import com.rana.hamza.tastyrecipes.util.Constants.Companion.QUERY_DIET
import com.rana.hamza.tastyrecipes.util.Constants.Companion.QUERY_Fill_INGREDIENTS
import com.rana.hamza.tastyrecipes.util.Constants.Companion.QUERY_NUMBER
import com.rana.hamza.tastyrecipes.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepo: DataStoreRepo
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE
    
     var networkStatus = false

    val readMealAndDietType = dataStoreRepo.readMealAndDietType
    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }


    fun applyQueries(): HashMap<String, String> {

        viewModelScope.launch {
            readMealAndDietType.collect{ value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType

            }
        }

        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_QUERY_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_Fill_INGREDIENTS] = "true"
        return queries
    }
    
    fun showNetworkStatus(){
        if (!networkStatus){
            Toast.makeText(getApplication(), "No Internet Connection!", Toast.LENGTH_SHORT).show()
        }
        if (networkStatus){
            Toast.makeText(getApplication(), "We are back online", Toast.LENGTH_SHORT).show()
        }
    }

}