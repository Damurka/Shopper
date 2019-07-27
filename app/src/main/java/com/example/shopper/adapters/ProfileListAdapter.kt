package com.example.shopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.databinding.ListItemProfileBinding
import com.example.shopper.models.Profile
import com.example.shopper.viewmodels.ProfileItemViewModel


class ProfileListAdapter(private val listener: (Profile) -> Unit) : ListAdapter<Profile, ProfileListAdapter.ViewHolder>(ProfileListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemProfileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { profile ->
            with(holder) {
                itemView.tag = profile
                bind(createOnClickListener(position), profile)
            }
        }
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            val item = getItem(position)
            listener(item)
        }
    }

    class ViewHolder(private val binding: ListItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, profile: Profile) {
            with(binding) {
                clickListener = listener
                viewModel = ProfileItemViewModel(profile)
                executePendingBindings()
            }
        }
    }
}

class ProfileListDiffCallback : DiffUtil.ItemCallback<Profile>() {

    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}