package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.databinding.ListItemFriendBinding
import com.example.shopper.models.Friend
import com.example.shopper.viewmodels.FriendViewModel


class ShareAdapter(private val listener: (Friend) -> Unit) : ListAdapter<Friend, ShareAdapter.ViewHolder>(FriendsDiffCallback()) {

    var isOwner = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { profile ->
            with(holder) {
                itemView.tag = profile
                bind(createOnClickListener(position), profile, isOwner)
            }
        }
    }

    fun getFriend(position: Int): Friend {
        return getItem(position)
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            val item = getItem(position)
            listener(item)
        }
    }

    class ViewHolder(private val binding: ListItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, friend: Friend, isOwner: Boolean) {
            with(binding) {
                clickListener = listener
                viewModel = FriendViewModel(friend, isOwner)
                executePendingBindings()
            }
        }
    }
}

class FriendsDiffCallback : DiffUtil.ItemCallback<Friend>() {

    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
        return oldItem == newItem
    }
}