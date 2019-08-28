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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shopper.adapters.ShoppingListAdapter
import com.example.shopper.databinding.FragmentHistoryBinding
import com.example.shopper.helpers.OnSwipe
import com.example.shopper.helpers.SwipeToDeleteAndArchiveCallback
import com.example.shopper.viewmodels.ArchiveViewModel
import com.example.shopper.viewmodels.ArchiveViewModelFactory
import com.example.shopper.viewmodels.AuthViewModel


/**
 * HistoryFragment
 *
 * Handles the History View for Shopping list that have been archived
 */
class HistoryFragment : Fragment() {

    /**
     * ViewModel that hold information about the authentication status
     */
    private val authViewModel: AuthViewModel by activityViewModels()
    /**
     * ViewModel that hold information about the archived shopping list
     */
    private val archiveViewModel: ArchiveViewModel by viewModels {
        ArchiveViewModelFactory(authViewModel.userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Initialize the toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.title = "History"

        // Initialize the adapter with the click handler
        val adapter = ShoppingListAdapter(authViewModel.userId) {
            val direction = HistoryFragmentDirections.actionHistoryDestToHistoryDetailDest(it)
            findNavController().navigate(direction)
        }
        binding.archiveList.adapter = adapter
        binding.archiveList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Initialize the callback to delete and unarchive the Shopping list
        val callback = SwipeToDeleteAndArchiveCallback(object: OnSwipe {
            override fun delete(position: Int) {
                val item = adapter.getShoppingList(position)
                archiveViewModel.deleteShoppingList(item, authViewModel.email)
            }

            override fun archive(position: Int) {
            }

        })

        // Attach the callback to the recyclerview
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.archiveList)

        // Retrieves and listens for changes in the archives list
        archiveViewModel.archivesLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }


}
