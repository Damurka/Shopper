package com.example.shopper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.shopper.adapters.ShoppingDetailAdapter
import com.example.shopper.databinding.FragmentHistoryBinding
import com.example.shopper.viewmodels.*

/**
 * Handles history details view
 */
class HistoryDetailFragment : Fragment() {

    /**
     * ViewModel that hold information about the authentication status
     */
    private val authViewModel: AuthViewModel by activityViewModels()

    /**
     * Argument sent from the History fragment listId
     * for Identifying the list for which to show the details
     */
    private val args: HistoryDetailFragmentArgs by navArgs()

    /**
     * ViewModel that holds information on the archived Shopping List
     */
    private val archiveViewModel: ArchiveViewModel by viewModels {
        ArchiveViewModelFactory(authViewModel.userId)
    }

    /**
     * ViewModel that holds information on a particular archived Shopping List
     */
    private val shoppingDetailsViewModel: ShoppingDetailsViewModel by viewModels {
        ShoppingDetailsViewModelFactory(args.listId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Initialize toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize and attach the adapter to recyclerview
        val adapter = ShoppingDetailAdapter {

        }
        binding.archiveList.adapter = adapter
        binding.archiveList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Retrieve the archived shopping list
        archiveViewModel.getArchiveItem(args.listId).observe(viewLifecycleOwner, Observer {
            activity.supportActionBar?.title = it.name
        })

        // Retrieve archived list items
        shoppingDetailsViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }


}
