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
import com.example.shopper.models.Message
import com.example.shopper.models.ShoppingList
import com.example.shopper.viewmodels.*

/**
 * ShareFragment
 *
 * Handles the Sharing View
 */
class ShareFragment : Fragment() {

    /**
     * ViewModel that hold information about the authentication status
     */
    private val authViewModel: AuthViewModel by activityViewModels()
    /**
     * Argument sent from the Shopper fragment listId
     * for Identifying the list for which to show the details
     */
    private val args: ShareFragmentArgs by navArgs()

    /**
     * ViewModel that holds information about the the Shopping List
     */
    private val shoppingListViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }

    /**
     * ViewModel used to send the notifications
     */
    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(authViewModel.userId)
    }
    /**
     * ViewModel used to hold information about the friends you have
     */
    private val friendsViewModel: ShareViewModel by viewModels {
        ShareViewModelFactory(authViewModel.userId, args.listId)
    }

    /**
     * Holds the information about a particular friends shopping list
     */
    private var shoppingLists= listOf<ShoppingList>()
    /**
     * Holds a particular shopping list that is being shared
     */
    private var shoppingList: ShoppingList? = null

    /**
     * Checks whether you own the list that you want to share
     */
    private val isOwner get() = shoppingList?.owner == authViewModel.email

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentShareBinding.inflate(inflater, container, false)

        // Initialize toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Share List"

        // Initialize the SHare Adapter for sharing a shopping list with other people
        val adapter = ShareAdapter{
            friendsViewModel.shareWith(it, shoppingList!!)
            val message = Message("Shared Shopping List", "${authViewModel.email} has shared ${shoppingList?.name} shopping list")
            notificationViewModel.addMessage(message, it.key!!)
        }
        binding.shareList.adapter = adapter
        binding.shareList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Initialize the delete/unfriend callback so that
        // Swipe left to unfriend a friend
        val callback = SwipeToDeleteCallback { position ->
            val item = adapter.getFriend(position)
            friendsViewModel.removeFriend(item, shoppingLists)
            val message = Message("You have been Unfriended", "You will no longer access ${authViewModel.email} shopping list")
            notificationViewModel.addMessage(message, item.key!!)
        }

        // Attach the callback to the recyclerview
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.shareList)

        // Retrieve the shopping List being Shared and identify if the current user is the owner of the list
        shoppingListViewModel.getShoppingListItem(args.listId).observe(viewLifecycleOwner, Observer {
            shoppingList = it

            adapter.isOwner = isOwner
            callback.isOwner = isOwner
            with(binding) {
                isCreator = isOwner
            }
        })

        // Retrieve all the shopping lists
        shoppingListViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            shoppingLists = it
        })

        // if the current user is the owner
        // Retrieve the list all my current friend
        friendsViewModel.result.observe(viewLifecycleOwner, Observer {
            if (isOwner) {
                adapter.submitList(it)
            }
        })

        // If the current is not the owner
        // Retrieve the list of people this list has been shared with
        friendsViewModel.sharedWithLiveData.observe(viewLifecycleOwner, Observer {
            if (!isOwner) {
                adapter.submitList(it)
            }
        })

        // set a click listener to the floating action bar
        // to navigate to the users list to add other users as friends
        binding.fab.setOnClickListener {
            val directions = ShareFragmentDirections.actionShareDestToAddFriendDest(args.listId)
            findNavController().navigate(directions)
        }

        return binding.root
    }


}
