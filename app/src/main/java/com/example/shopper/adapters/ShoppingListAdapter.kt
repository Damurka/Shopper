package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.ShopperFragmentDirections
import com.example.shopper.databinding.ListItemShoppingBinding
import com.example.shopper.models.ShoppingList
import com.example.shopper.viewmodels.ShoppingListItemViewModel

class ShoppingListAdapter() : ListAdapter<ShoppingList, ShoppingListAdapter.ViewHolder>(ShoppingListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShoppingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { recipe ->
            with(holder) {
                itemView.tag = recipe
                bind(createOnClickListener(position), recipe)
            }
        }
    }

    fun getShoppingList(position: Int): ShoppingList {
        return getItem(position)
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            val item = getItem(position)
            val direction = ShopperFragmentDirections.actionShoppingDestToShoppingDetailsDest(item.key)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(private val binding: ListItemShoppingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, shoppingList: ShoppingList) {
            with(binding) {
                clickListener = listener
                viewModel = ShoppingListItemViewModel(shoppingList)
                executePendingBindings()
            }
        }
    }
}

private class ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingList>() {

    override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem == newItem
    }
}