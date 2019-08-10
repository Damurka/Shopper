package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.CategoryFragmentDirections
import com.example.shopper.databinding.ListItemRecipeBinding
import com.example.shopper.models.Recipe
import com.example.shopper.models.RecipeCover
import com.example.shopper.viewmodels.RecipeItemViewModel


class RecipeAdapter: ListAdapter<RecipeCover, RecipeAdapter.ViewHolder>(RecipeDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { recipe ->
            with(holder) {
                itemView.tag = recipe
                bind(createOnClickListener(recipe.recipe!!), recipe.recipe!!)
            }
        }
    }

    private fun createOnClickListener(recipe: Recipe): View.OnClickListener {
        return View.OnClickListener {
            val direction = CategoryFragmentDirections.actionCategoryDestToRecipeDest(recipe)
            it.findNavController().navigate(direction)
        }

    }

    class ViewHolder(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, recipe: Recipe) {
            with(binding) {
                clickListener = listener
                viewModel = RecipeItemViewModel(recipe)
                executePendingBindings()
            }
        }
    }
}

private class RecipeDiffCallback : DiffUtil.ItemCallback<RecipeCover>() {

    override fun areItemsTheSame(oldItem: RecipeCover, newItem: RecipeCover): Boolean {
        return oldItem.recipe?.label == newItem.recipe?.label
    }

    override fun areContentsTheSame(oldItem: RecipeCover, newItem: RecipeCover): Boolean {
        return oldItem == newItem
    }
}