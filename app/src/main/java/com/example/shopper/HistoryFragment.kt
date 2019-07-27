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
import com.example.shopper.helpers.SwipeToDeleteCallback
import com.example.shopper.viewmodels.ArchiveViewModel
import com.example.shopper.viewmodels.ArchiveViewModelFactory
import com.example.shopper.viewmodels.AuthViewModel


class HistoryFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()
    private val archiveViewModel: ArchiveViewModel by viewModels {
        ArchiveViewModelFactory(authViewModel.userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.title = "History"

        val adapter = ShoppingListAdapter(authViewModel.userId) {
            val direction = HistoryFragmentDirections.actionHistoryDestToHistoryDetailDest(it)
            findNavController().navigate(direction)
        }
        binding.archiveList.adapter = adapter
        binding.archiveList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        val callback = SwipeToDeleteCallback {
            val item = adapter.getShoppingList(it)
            archiveViewModel.deleteShoppingList(item, authViewModel.email)
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.archiveList)

        archiveViewModel.archivesLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }


}
