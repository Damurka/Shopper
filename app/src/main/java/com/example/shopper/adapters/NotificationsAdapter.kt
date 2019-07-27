package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.databinding.ListItemNotificationsBinding
import com.example.shopper.models.Message
import com.example.shopper.viewmodels.NotificationItemViewModel


class NotificationsAdapter : ListAdapter<Message, NotificationsAdapter.ViewHolder>(NotificationsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemNotificationsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { message ->
            with(holder) {
                itemView.tag = message
                bind(createOnClickListener(position), message)
            }
        }
    }

    fun getMessage(position: Int): Message {
        return getItem(position)
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            val item = getItem(position)
            // listener(item.key!!)
        }
    }

    class ViewHolder(private val binding: ListItemNotificationsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, message: Message) {
            with(binding) {
                clickListener = listener
                viewModel = NotificationItemViewModel(message)
                executePendingBindings()
            }
        }
    }
}

private class NotificationsDiffCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}