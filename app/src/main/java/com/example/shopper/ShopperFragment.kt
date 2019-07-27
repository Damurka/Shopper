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
import com.example.shopper.models.Message
import com.example.shopper.models.ShoppingList
import com.example.shopper.viewmodels.*


class ShopperFragment : Fragment() {

    private var hasRecordPermission = false

    private val authViewModel: AuthViewModel by activityViewModels()
    private val shoppingViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }
    private val voiceVoiceModel: VoiceViewModel by viewModels {
        VoiceViewModelFactory(requireActivity().application)
    }
    private val notificationViewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(requireContext(), authViewModel.userId)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentShopperBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.title = "Shopping Lists"

        val adapter = ShoppingListAdapter(authViewModel.userId) {
            val direction = ShopperFragmentDirections.actionShoppingDestToShoppingDetailsDest(it)
            findNavController().navigate(direction)
        }

        binding.shoppingList.adapter = adapter
        binding.shoppingList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

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
                    notificationViewModel.addMessage(item.userId, Message("Archiving Request", "${authViewModel.email} is requesting that ${item.name} Shopping List be archived"))
                }
            }

        })

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.shoppingList)

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

        shoppingViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                //TODO: Check is granted
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkPermission() {
        val context = requireActivity().applicationContext
        hasRecordPermission = ContextCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_GRANTED
    }

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
