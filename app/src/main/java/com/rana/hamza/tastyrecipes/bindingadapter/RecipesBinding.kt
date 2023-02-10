package com.rana.hamza.tastyrecipes.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rana.hamza.tastyrecipes.data.database.RecipeEntity
import com.rana.hamza.tastyrecipes.models.FoodRecipe
import com.rana.hamza.tastyrecipes.util.NetworkResult

class RecipesBinding {

    companion object{


        @BindingAdapter("readApiResponse","readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibilty(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
        database: List<RecipeEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility= View.VISIBLE
            }else if (apiResponse is NetworkResult.Loading){
                imageView.visibility= View.INVISIBLE
            }else if (apiResponse is NetworkResult.Success){
                 imageView.visibility= View.INVISIBLE
            }

        }

        @BindingAdapter("readApiResponse2","readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextVisibilty(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipeEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()  ){
                textView.visibility= View.VISIBLE
                textView.text= apiResponse.message.toString()
            }else if (apiResponse is NetworkResult.Loading){
                textView.visibility= View.INVISIBLE
            }else if (apiResponse is NetworkResult.Success){
                textView.visibility= View.INVISIBLE
            }
        }
    }


}