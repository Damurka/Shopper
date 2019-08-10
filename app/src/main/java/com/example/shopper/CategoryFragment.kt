package com.example.shopper

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.shopper.adapters.RecipeAdapter
import com.example.shopper.databinding.FragmentCategoryBinding
import com.example.shopper.viewmodels.RecipeViewModel

class CategoryFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCategoryBinding.inflate(inflater, container, false)

        val adapter = RecipeAdapter()
        binding.recipeList.adapter = adapter

        recipeViewModel.recipeLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            for (recipe in it) {
                Log.e("CategoryFragment", recipe.recipe?.label)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

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
