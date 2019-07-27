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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shopper.adapters.ShareAdapter
import com.example.shopper.databinding.FragmentShareBinding
import com.example.shopper.helpers.SwipeToDeleteCallback
import com.example.shopper.models.ShoppingList
import com.example.shopper.viewmodels.*


class ShareFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()
    private val args: ShareFragmentArgs by navArgs()
    private val shoppingListViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }
    private val friendsViewModel: ShareViewModel by viewModels {
        ShareViewModelFactory(authViewModel.userId)
    }

    private val sharedWithViewModel: SharedWithViewModel by viewModels {
        SharedWithViewModelFactory(args.listId)
    }

    private var shoppingLists= listOf<ShoppingList>()
    private var shoppingList: ShoppingList? = null

    private val isOwner get() = shoppingList?.owner == authViewModel.email

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentShareBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Share List"

        val adapter = ShareAdapter{
            friendsViewModel.shareWith(it, shoppingList!!)
        }

        binding.shareList.adapter = adapter
        binding.shareList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        val callback = SwipeToDeleteCallback {
                val item = adapter.getFriend(it)
                friendsViewModel.removeFriend(item, shoppingLists)
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.shareList)

        shoppingListViewModel.getShoppingListItem(args.listId).observe(viewLifecycleOwner, Observer {
            shoppingList = it

            adapter.isOwner = isOwner
            callback.isOwner = isOwner
            with(binding) {
                isCreator = isOwner
            }
        })

        shoppingListViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            shoppingLists = it
        })

        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner, Observer {
            if (isOwner) {
                adapter.submitList(it)
            }
        })

        sharedWithViewModel.sharedWithLiveData.observe(viewLifecycleOwner, Observer {
            if (!isOwner) {
                adapter.submitList(it)
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_share_dest_to_add_friend_dest)
        }

        return binding.root
    }


}
