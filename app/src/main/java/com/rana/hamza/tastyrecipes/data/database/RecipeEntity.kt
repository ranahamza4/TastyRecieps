package com.rana.hamza.tastyrecipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rana.hamza.tastyrecipes.models.FoodRecipe
import com.rana.hamza.tastyrecipes.util.Constants

@Entity(tableName = Constants.TABLE_NAME)
class RecipeEntity(
var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id:Int =0
}