package com.example.shopper

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shopper.adapters.ShoppingListAdapter
import com.example.shopper.databinding.FragmentShopperBinding
import com.example.shopper.helpers.OnSwipe
import com.example.shopper.helpers.SwipeToDeleteAndArchiveCallback
import com.example.shopper.helpers.capitalizeWords
import com.example.shopper.models.*
import com.example.shopper.viewmodels.*


/**
 * ShopperFragment
 *
 * For handling the shopping List view
 */
class ShopperFragment : Fragment() {

    /**
     * Checks whether the app has the Manifest.permission.RECORD_AUDIO permission
     * Initialized to false
     */
    private var hasRecordPermission = false

    /**
     * ViewModel that hold information about the authentication status
     */
    private val authViewModel: AuthViewModel by activityViewModels()

    /**
     * ViewModel that holds information about the the Shopping List
     */
    private val shoppingViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }

    /**
     * ViewModel that assists to convert voice to words
     */
    private val voiceVoiceModel: VoiceViewModel by viewModels {
        VoiceViewModelFactory(requireActivity().application)
    }

    /**
     * ViewModel used to send the notifications
     */
    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(authViewModel.userId)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentShopperBinding.inflate(inflater, container, false)

        // Initialize the toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.title = "Shopping Lists"

        // Initialize the Shopping List Adapter
        val adapter = ShoppingListAdapter(authViewModel.userId) {
            val direction = ShopperFragmentDirections.actionShoppingDestToShoppingDetailsDest(it)
            findNavController().navigate(direction)
        }
        binding.shoppingList.adapter = adapter
        binding.shoppingList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))


        // Callback to the the swipe right and left
        // Swipe right to delete and left to  archive
        val callback = SwipeToDeleteAndArchiveCallback(object: OnSwipe {
            override fun delete(position: Int) {
                val item = adapter.getShoppingList(position)
                shoppingViewModel.deleteShoppingList(item)
            }

            override fun archive(position: Int) {
                val item = adapter.getShoppingList(position)
                if (item.userId == authViewModel.userId) {
                    shoppingViewModel.archiveShoppingList(item)
                } else {
                    adapter.notifyItemChanged(position)
                    Toast.makeText(requireContext(), "Your archive request sent to the owner of the list", Toast.LENGTH_LONG).show()
                    val message = Message("Archiving Request", "${authViewModel.email} is requesting that ${item.name} Shopping List be archived")
                    notificationViewModel.addMessage(message, item.userId)
                }
            }

        })

        // Attach the call back to the recyclerview
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.shoppingList)

        // Listen for floating action bar been pressed and
        // initialize the microphone listening
        binding.fab.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_UP -> {
                    voiceVoiceModel.stopRecognition()
                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    if (!hasRecordPermission) {
                        requestRecordPermissions()
                        return@setOnTouchListener false
                    }

                    voiceVoiceModel.startRecognition()
                    true
                }
                else -> false
            }
        }

        // Observe changes in the shopping list and update the shopping list adapter
        shoppingViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Observe whether someone is talking return the words that were said
        voiceVoiceModel.recognizedLiveData.observe(viewLifecycleOwner, Observer {
            if (voiceVoiceModel.actualSpeech) {
                if (!it.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "You said: $it", Toast.LENGTH_LONG).show()
                    val list = ShoppingList(name = it.capitalizeWords(), owner = authViewModel.email, userId = authViewModel.userId)
                    shoppingViewModel.addShoppingList(list)
                } else {
                    Toast.makeText(requireContext(), "Error: Please say that again", Toast.LENGTH_LONG).show()
                }

                voiceVoiceModel.actualSpeech = false
            }
        })

        voiceVoiceModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
        })

        checkPermission()

        return binding.root
    }

    /**
     * Reads the result on whether the Manifest.permission.RECORD_AUDIO has been granted or denied
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                //TODO: Check is granted
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Check if the app has the Manifest.permission.RECORD_AUDIO permidion
     */
    private fun checkPermission() {
        val context = requireActivity().applicationContext
        hasRecordPermission = ContextCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests for the Manifest.permission.RECORD_AUDIO permission
     */
    private fun requestRecordPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
            permissions,
            REQUEST_CODE
        )
    }

    companion object {
        private const val REQUEST_CODE = 1

        private val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO)
    }
}
