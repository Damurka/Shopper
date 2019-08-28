package com.example.shopper

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.shopper.adapters.RecipeAdapter
import com.example.shopper.databinding.FragmentCategoryBinding
import com.example.shopper.viewmodels.RecipeViewModel

/**
 * CategoryFragment
 *
 * Handles the search view to search for recipes
 */
class CategoryFragment : Fragment() {

    /**
     * ViewModel that hold information about the authentication status
     */
    private val recipeViewModel: RecipeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCategoryBinding.inflate(inflater, container, false)

        // Initialize the Recipes adapter
        val adapter = RecipeAdapter()
        binding.recipeList.adapter = adapter

        // Retrieves the list of recipes and updates the view
        recipeViewModel.recipeLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Set the menu for the fragment
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        // Initialize the search bar for this fragment
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    recipeViewModel.getRecipes(query!!)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
            setIconifiedByDefault(false)
        }
    }
}
