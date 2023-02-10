package com.rana.hamza.tastyrecipes.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipeEntity: RecipeEntity)


    @Query("Select * from recipe_table Order By id ASC")
      fun fetchRecipes(): Flow<List<RecipeEntity>>

}