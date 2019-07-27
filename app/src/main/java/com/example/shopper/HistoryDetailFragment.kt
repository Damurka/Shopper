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


class HistoryDetailFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()
    private val args: HistoryDetailFragmentArgs by navArgs()
    private val archiveViewModel: ArchiveViewModel by viewModels {
        ArchiveViewModelFactory(authViewModel.userId)
    }
    private val shoppingDetailsViewModel: ShoppingDetailsViewModel by viewModels {
        ShoppingDetailsViewModelFactory(args.listId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val adapter = ShoppingDetailAdapter {

        }
        binding.archiveList.adapter = adapter
        binding.archiveList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        archiveViewModel.getArchiveItem(args.listId).observe(viewLifecycleOwner, Observer {
            activity.supportActionBar?.title = it.name
        })

        shoppingDetailsViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }


}
