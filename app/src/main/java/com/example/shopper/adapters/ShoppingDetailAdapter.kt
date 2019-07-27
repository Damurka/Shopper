package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.adapters.ShoppingDetailAdapter.*
import com.example.shopper.databinding.ListItemShoppingItemBinding
import com.example.shopper.models.ShoppingItem
import com.example.shopper.viewmodels.ShoppingDetailsItemViewModel


class ShoppingDetailAdapter(private val listener: (ShoppingItem) -> Unit): ListAdapter<ShoppingItem, ViewHolder>(ShoppingItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemShoppingItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                itemView.tag = item
                bind(createOnClickListener(position), item)
            }
        }
    }

    fun getShoppingItem(position: Int): ShoppingItem {
        return getItem(position)
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            val item = getItem(position)
            listener(item)
        }
    }

    class ViewHolder(private val binding: ListItemShoppingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, shoppingItem: ShoppingItem) {
            with(binding) {
                viewModel = ShoppingDetailsItemViewModel(shoppingItem)
                clickListener = listener
                executePendingBindings()
            }
        }
    }
}

private class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {

    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}