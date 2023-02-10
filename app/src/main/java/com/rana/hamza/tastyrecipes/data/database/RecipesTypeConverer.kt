package com.rana.hamza.tastyrecipes.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rana.hamza.tastyrecipes.models.FoodRecipe

class RecipesTypeConverer {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe{
       val typeToken= object :TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,typeToken)
    }
}