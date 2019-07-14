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
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shopper.adapters.ShoppingListAdapter
import com.example.shopper.databinding.FragmentShopperBinding
import com.example.shopper.helpers.ShopperSwipeToDeleteCallback
import com.example.shopper.helpers.capitalizeWords
import com.example.shopper.models.ShoppingList
import com.example.shopper.viewmodels.*


class ShopperFragment : Fragment() {

    private var hasRecordPermission = false

    private val authViewModel: AuthViewModel by activityViewModels()
    private val shoppingViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }
    private val voiceVoiceModel: VoiceViewModel by viewModels {
        VoiceViewModelFactory(requireContext())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentShopperBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val adapter = ShoppingListAdapter()
        binding.shoppingList.adapter = adapter

        val callback = ShopperSwipeToDeleteCallback {
            val item = adapter.getShoppingList(it)
            shoppingViewModel.deleteShoppingList(item.key)
        }

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
            val list = it as List<ShoppingList>
            if (!list.isNullOrEmpty()) {
                adapter.submitList(it)
            }
        })

        voiceVoiceModel.recognizedLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank()) {
                Toast.makeText(requireContext(), "You said: $it", Toast.LENGTH_LONG).show()
                val list = ShoppingList(name = it.capitalizeWords(), owner = authViewModel.email)
                shoppingViewModel.addShoppingList(list)
            } else {
                Toast.makeText(requireContext(), "Error: Please say that again", Toast.LENGTH_LONG).show()
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
