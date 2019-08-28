package com.example.shopper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.shopper.adapters.NotificationsAdapter
import com.example.shopper.databinding.FragmentNotificationsBinding
import com.example.shopper.viewmodels.AuthViewModel
import com.example.shopper.viewmodels.NotificationViewModel
import com.example.shopper.viewmodels.NotificationViewModelFactory


/**
 * Handles the notification view
 */
class NotificationsFragment : Fragment() {

    /**
     * ViewModel that hold information about the authentication status
     */
    private val authViewModel: AuthViewModel by activityViewModels()

    /**
     * ViewModel that hold information about the notifications
     */
    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(authViewModel.userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        // Initialize the toolbar and set the title
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.title = "Notifications"

        // Initialize the adapter and attach to the recyclerview
        val adapter = NotificationsAdapter()
        binding.notificationsList.adapter = adapter
        binding.notificationsList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Retrieve and update the recyclerview with the notifications
        notificationViewModel.notificationLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }

}
