package com.example.shopper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.shopper.adapters.IngredientAdapter
import com.example.shopper.databinding.FragmentRecipeBinding
import com.example.shopper.models.ShoppingItem
import com.example.shopper.models.ShoppingList
import com.example.shopper.viewmodels.AuthViewModel
import com.example.shopper.viewmodels.ShoppingListViewModel
import com.example.shopper.viewmodels.ShoppingListViewModelFactory


/**
 * RecipeFragment
 *
 * Handles the recipe details
 */
class RecipeFragment : Fragment() {

    /**
     * ViewModel that hold information about the authentication status
     * of the current user
     */
    private val authViewModel: AuthViewModel by activityViewModels()

    /**
     * ViewModel that hold information about the shopping lists for the current user
     */
    private val shoppingViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }

    /**
     * Argument sent from the CategoryFragment Recipe
     * for which to show the Recipe details
     */
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentRecipeBinding.inflate(inflater, container, false).apply {
            recipe = args.recipe
        }

        // Initialize the toolbar
        val activity = requireActivity() as AppCompatActivity
        val toolbar = activity.supportActionBar as ActionBar
        toolbar.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = args.recipe.label

        // Initialize the ingredient Adapter and attach it to the recycler view
        val adapter = IngredientAdapter()
        binding.ingredientList.adapter = adapter
        binding.ingredientList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        adapter.submitList(args.recipe.ingredients)

        // Add a clicklistener to the AddShopping button to a particular recipe as a shopping list
        binding.addShopping.setOnClickListener {
            val shoppingList = ShoppingList(args.recipe.label!!, authViewModel.email, authViewModel.userId)
            val items = mutableListOf<ShoppingItem>()
            for (ingredient in args.recipe.ingredients) {
                val item = ShoppingItem(ingredient, authViewModel.email)
                items.add(item)
            }

            shoppingViewModel.addShoppingList(shoppingList, items)
            Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_LONG).show()
        }

        // Set a click listener to the floating action bar to navigate to the recipes website
        binding.fab.setOnClickListener {
            val open = Intent(Intent.ACTION_VIEW)
            open.data = Uri.parse(args.recipe.url)

            startActivity(open)
        }

        return binding.root
    }

}
