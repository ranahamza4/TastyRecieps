package com.rana.hamza.tastyrecipes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.rana.hamza.tastyrecipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.rana.hamza.tastyrecipes.util.Constants.Companion.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@ActivityRetainedScoped
class DataStoreRepo @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys{
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

     val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCE_NAME
    )

   suspend fun  saveMealAndDietType(mealType:String,mealTypeId:Int,dietType:String,dietTypeId:Int){
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType]= mealType
            preferences[PreferenceKeys.selectedMealTypeId]= mealTypeId
            preferences[PreferenceKeys.selectedDietType]= dietType
            preferences[PreferenceKeys.selectedDietTypeId]= dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }

        }
        .map { prefernce ->
            val mealType = prefernce[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val mealTypeId = prefernce[PreferenceKeys.selectedMealTypeId] ?: 0
            val dietType = prefernce[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val dietTypeId = prefernce[PreferenceKeys.selectedDietTypeId]?: 0

            MealAndDietType(
                mealType,
                mealTypeId,
                dietType,
                dietTypeId
            )
        }


}


data class MealAndDietType(
    val selectedMealType:String,
    val selectedMealTypeId:Int,
    val selectedDietType:String,
    val selectedDietTypeId:Int
)