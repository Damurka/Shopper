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


class AddFriendFragment : Fragment() {

    private val args: AddFriendFragmentArgs by navArgs()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val profileListViewModel: ProfileListViewModel by activityViewModels()
    private val friendsViewModel: ShareViewModel by viewModels {
        ShareViewModelFactory(authViewModel.userId, args.listId)
    }
    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(authViewModel.userId)
    }

    private var friends = listOf<Friend>()

    private lateinit var adapter: ProfileListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAddFriendBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Add Friend"

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

        profileListViewModel.profileListLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })

        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner, Observer {
            friends = it
        })

        return binding.root
    }

}
