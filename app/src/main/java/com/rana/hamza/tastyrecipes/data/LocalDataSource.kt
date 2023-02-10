package com.rana.hamza.tastyrecipes.data

import com.rana.hamza.tastyrecipes.data.database.RecipeEntity
import com.rana.hamza.tastyrecipes.data.database.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    suspend fun insertRecipes(recipeEntity: RecipeEntity){
        recipesDao.insertRecipes(recipeEntity)
    }

   suspend  fun readDatabase(): Flow<List<RecipeEntity>>{
        return recipesDao.fetchRecipes()
    }

}