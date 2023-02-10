package com.rana.hamza.tastyrecipes.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rana.hamza.tastyrecipes.R
import com.rana.hamza.tastyrecipes.adapters.RecipeAdapter
import com.rana.hamza.tastyrecipes.databinding.FragmentRecipesBinding
import com.rana.hamza.tastyrecipes.util.NetworkListener
import com.rana.hamza.tastyrecipes.util.NetworkResult
import com.rana.hamza.tastyrecipes.util.observeOnce
import com.rana.hamza.tastyrecipes.viewModels.MainViewModel
import com.rana.hamza.tastyrecipes.viewModels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG= "RecipesFragment"

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipesBinding?= null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipeAdapter() }

    private lateinit var networkListener: NetworkListener
    var mMenu:Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner=this
        binding.mainViewModel= mainViewModel


        binding.recipesFab.setOnClickListener{
            if (recipesViewModel.networkStatus){
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet2)
            }else{
                recipesViewModel.showNetworkStatus()
            }

       }
        setupRecyclerView()


        networkListener= NetworkListener()

        lifecycleScope.launch{
            networkListener.checkNetworkAvailability(requireContext()).collect{ state ->
                recipesViewModel.networkStatus= state
                recipesViewModel.showNetworkStatus()
                readDatabase()
            }
        }

        return binding.root
    }

    private fun setUpMenu() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.recipes_menu, menu)
              mMenu =menu

                val searchBar =  mMenu!!.findItem(R.id.menu_search)
                val searchView = searchBar.actionView as? SearchView
                searchView?.setOnQueryTextListener(queryListener)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_search -> {
                        // clearCompletedTasks()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



    }

    private val queryListener =object :SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
           return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }

    }

    private fun readDatabase() {
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
            if (database.isNotEmpty() && !args.backFromBottomSheet) {
                Log.d(TAG, "readDatabase: called")
                mAdapter.setData(database[0].foodRecipe)
                hideShimmerEffect()
            }else{
                requestApiData()
            }
        }
    }
    private fun loadDataFromCache() {
        mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
            if (database.isNotEmpty()) {
                Log.d(TAG, "readDatabase: called")
                mAdapter.setData(database[0].foodRecipe)
                hideShimmerEffect()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun requestApiData() {
        Log.d(TAG, "requestApiData: called")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())

        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenu()

    }



    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
_binding=null
    }


}