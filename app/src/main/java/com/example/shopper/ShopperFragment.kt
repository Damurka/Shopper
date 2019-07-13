package com.example.shopper

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.adapters.ShoppingListAdapter
import com.example.shopper.databinding.FragmentShopperBinding
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
        val touchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
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
                val list = ShoppingList(name = it, owner = authViewModel.email)
                shoppingViewModel.addShoppingList(list)
            } else {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
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

    private inner class SwipeToDeleteCallback(private val adapter: ShoppingListAdapter)
        : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){

        private val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
        private val background = ColorDrawable(Color.RED)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = adapter.getShoppingList(viewHolder.adapterPosition)
            shoppingViewModel.deleteShoppingList(item.key)
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            val view = viewHolder.itemView
            val backgroundOffset = 20
            val iconMargin = (view.height - icon!!.intrinsicHeight) / 2
            val iconTop = view.top + (view.height - icon.intrinsicHeight) / 2
            val iconBottom = iconTop + icon.intrinsicHeight

            when {
                dX > 0 -> {// Swiping to the right
                    val iconLeft = view.left + iconMargin + icon.intrinsicWidth
                    val iconRight = view.left + iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(view.left, view.top, view.left + dX.toInt() + backgroundOffset, view.bottom)
                }
                dX < 0 -> {// Swiping to the Left
                    val iconLeft = view.right - iconMargin - icon.intrinsicWidth
                    val iconRight = view.right - iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(view.right + dX.toInt() - backgroundOffset, view.top, view.right, view.bottom)
                }
                else -> // View is unswiped
                    background.setBounds(0, 0, 0, 0)
            }

            background.draw(c)
            icon.draw(c)
        }

    }
}
