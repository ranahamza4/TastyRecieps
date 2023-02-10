package com.rana.hamza.tastyrecipes.bottomSheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.khaleeq.hamza.foodx.R
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.rana.hamza.tastyrecipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.rana.hamza.tastyrecipes.viewModels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*

@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {

    private  val recipesViewModel : RecipesViewModel by viewModels()

    private var mealTypeChip =DEFAULT_MEAL_TYPE
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0
    private var mealTypeChipId = 0
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.recipes_bottom_sheet, container, false)


        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, mView.meal_type_chipGroup!!)
            updateChip(value.selectedDietTypeId, mView.diet_type_chipGroup!!)
        }



        mView.meal_type_chipGroup.setOnCheckedStateChangeListener{ group,_->
            val chip  = group.findViewById<Chip>(group.checkedChipId)
            val selectedMealType = chip.text.toString()
            mealTypeChip=selectedMealType
            mealTypeChipId = group.checkedChipId


        }

        mView.diet_type_chipGroup.setOnCheckedStateChangeListener{ group, _ ->
            val chip  = group.findViewById<Chip>(group.checkedChipId)
            val selectedMealType = chip.text.toString()
            dietTypeChip=selectedMealType
            dietTypeChipId = group.checkedChipId


        }

        mView.apply_btn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId,
            )

         val action =  RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment2()
            findNavController().navigate(action)
        }



    return mView
    }

    private fun updateChip(id: Int, chipGroup: ChipGroup) {

        Log.d("AAA", "id = $id")

        if (id !=0){
            try {


               chipGroup.check(id)
            }catch (e:Exception){
                Log.d("BottomSheet", "updateChip: ${e.message.toString()}")
            }
        }
    }


}