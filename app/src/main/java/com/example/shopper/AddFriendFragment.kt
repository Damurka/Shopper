package com.example.shopper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.shopper.adapters.ProfileListAdapter
import com.example.shopper.databinding.FragmentAddFriendBinding
import com.example.shopper.models.Friend
import com.example.shopper.models.Message
import com.example.shopper.viewmodels.*


/**
 * AddFriendFragment
 *
 * Handles users view that shows all users of the app
 */
class AddFriendFragment : Fragment() {

    /**
     * Argument sent from the Shopper fragment listId
     * for Identifying the list for which to show the details
     */
    private val args: AddFriendFragmentArgs by navArgs()

    /**
     * ViewModel that hold information about the authentication status
     * of the current user
     */
    private val authViewModel: AuthViewModel by activityViewModels()

    /**
     * ViewModel holds information of all users of this app
     */
    private val profileListViewModel: ProfileListViewModel by activityViewModels()

    /**
     * ViewModel holds information of friends of the current user
     */
    private val friendsViewModel: ShareViewModel by viewModels {
        ShareViewModelFactory(authViewModel.userId, args.listId)
    }

    /**
     * ViewModel used to send the notifications
     */
    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(authViewModel.userId)
    }

    /**
     * Holds information about the friends of the current user
     */
    private var friends = listOf<Friend>()

    /**
     * Adapter for all the users
     */
    private lateinit var adapter: ProfileListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAddFriendBinding.inflate(inflater, container, false)

        // Initialize the toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Add Friend"

        // Initialize the adapter along with the click listener
        adapter = ProfileListAdapter { profile ->
            val isFriend = friends.find {
                it.key == profile.key
            }

            when {
                profile.key == authViewModel.userId -> Toast.makeText(requireContext(), "You cannot add yourself", Toast.LENGTH_LONG).show()
                isFriend != null -> Toast.makeText(requireContext(), "Already your friends", Toast.LENGTH_LONG).show()
                else -> {
                    val friend = Friend(profile.email, profile.name)
                    friendsViewModel.addFriend(profile.key!!, friend)
                    findNavController().navigateUp()
                    val message = Message("Added as friend", "${authViewModel.email} has added you as a friend")
                    notificationViewModel.addMessage(message, profile.key!!)
                }
            }
        }
        binding.profileList.adapter = adapter
        binding.profileList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Retrieves and Listens for changes in the current users
        profileListViewModel.profileListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Retrieves and Listens for changes in the current user's friends
        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner, Observer {
            friends = it
        })

        return binding.root
    }

}
