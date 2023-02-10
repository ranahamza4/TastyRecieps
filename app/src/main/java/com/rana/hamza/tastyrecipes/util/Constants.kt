package com.rana.hamza.tastyrecipes.util

class Constants {

    companion object {

        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "a9e6e59a170f41d9ad68cf3e9035dd6a"


        //API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_Fill_INGREDIENTS = "fillIngredients"


        // Room Varibale
        const val DATABASE_NAME= "recipe_database"
        const val TABLE_NAME= "recipe_table"


        //bottom sheet and preference
        const val PREFERENCE_NAME=" tastyRecipe_preference"
        const val DEFAULT_MEAL_TYPE ="main course"
        const val DEFAULT_DIET_TYPE ="gluten free"
        const val DEFAULT_QUERY_NUMBER ="50"
        const val PREFERENCES_MEAL_TYPE="mealType"
        const val PREFERENCES_MEAL_TYPE_ID="mealTypeId"
        const val PREFERENCES_DIET_TYPE="dietType"
        const val PREFERENCES_DIET_TYPE_ID="dietTypeId"

    }
}