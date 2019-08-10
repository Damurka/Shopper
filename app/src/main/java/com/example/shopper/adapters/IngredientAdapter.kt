package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.databinding.ListItemIngredientBinding


class IngredientAdapter: ListAdapter<String, IngredientAdapter.ViewHolder>(IngredientDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { ingredient ->
            with(holder) {
                itemView.tag = ingredient
                bind(ingredient)
            }
        }
    }

    class ViewHolder(private val binding: ListItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: String) {
            with(binding) {
                item = ingredient
                executePendingBindings()
            }
        }
    }
}

private class IngredientDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}