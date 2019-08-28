package com.example.shopper

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shopper.adapters.ShoppingDetailAdapter
import com.example.shopper.databinding.FragmentShoppingDetailsBinding
import com.example.shopper.helpers.SwipeToDeleteCallback
import com.example.shopper.helpers.capitalizeWords
import com.example.shopper.models.ShoppingItem
import com.example.shopper.viewmodels.*


/**
 * ShoppingDetailsFragment
 *
 * For Handing the Shopping list details view
 */
class ShoppingDetailsFragment : Fragment() {

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
     * Argument sent from the Shopper fragment listId
     * for Identifying the list for which to show the details
     */
    private val args: ShoppingDetailsFragmentArgs by navArgs()

    /**
     * ViewModels holds the details about a particular shopping list
     */
    private val shoppingListViewModel: ShoppingListViewModel by viewModels {
        ShoppingListViewModelFactory(authViewModel.userId)
    }

    /**
     * ViewModel that holds information about the the Shopping List
     */
    private val shoppingDetailsViewModel: ShoppingDetailsViewModel by viewModels {
        ShoppingDetailsViewModelFactory(args.listId)
    }

    /**
     * ViewModel that assists to convert voice to words
     */
    private val voiceVoiceModel: VoiceViewModel by viewModels {
        VoiceViewModelFactory(requireActivity().application)
    }

    /**
     * Callback for deleting a shopping item
     */
    private lateinit var callback: SwipeToDeleteCallback

    /**
     * Shopping Fragment Reference
     */
    private lateinit var binding: FragmentShoppingDetailsBinding

    /**
     * Menu reference
     */
    private lateinit var myMenu: Menu

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShoppingDetailsBinding.inflate(inflater, container, false)

        // Initialize toolbar
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize the Shopping detail adapter
        val adapter = ShoppingDetailAdapter {
            if (!callback.isOwner) {
                if (it.bought) {
                    val bought = ShoppingItem(it.name, it.owner)
                    bought.key = it.key
                    shoppingDetailsViewModel.buyItem(bought)
                } else {
                    val buying = ShoppingItem(it.name, it.owner, true, authViewModel.email)
                    buying.key = it.key
                    shoppingDetailsViewModel.buyItem(buying)
                }
            }
        }
        binding.shoppingList.adapter = adapter
        binding.shoppingList.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Initialize the Swipe to delete callback
        callback = SwipeToDeleteCallback {
            val item = adapter.getShoppingItem(it)
            shoppingDetailsViewModel.deleteShoppingItem(item.key!!)
        }

        // Attach the callback to to the recycler view
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.shoppingList)


        // add a touch listener to the floating action bar
        // so as to initialize voice listener
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

        toggleShopping()

        // Retrieves a shopping list item and set the title in the toolbar
        shoppingListViewModel.getShoppingListItem(args.listId).observe(viewLifecycleOwner, Observer {
            activity.supportActionBar?.title = it.name
        })

        // Listens for changes in the shopping items and updates the list
        shoppingDetailsViewModel.shopperLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Listens for data from voice
        voiceVoiceModel.recognizedLiveData.observe(viewLifecycleOwner, Observer {
            if (voiceVoiceModel.actualSpeech) {
                if (!it.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "You said: $it", Toast.LENGTH_LONG).show()
                    val item = ShoppingItem(name = it.capitalizeWords(), owner = authViewModel.email)
                    shoppingDetailsViewModel.addShoppingListItem(item)
                } else {
                    Toast.makeText(requireContext(), "Error: Please say that again", Toast.LENGTH_LONG).show()
                }

                voiceVoiceModel.actualSpeech = false
            }
        })

        checkPermission()

        // Sets the menu on this fragment
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopper_details, menu)
        myMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val direction = ShoppingDetailsFragmentDirections.actionShoppingDetailsDestToShareDest(args.listId)
                findNavController().navigate(direction)
                true
            }
            R.id.action_shop -> {
                callback.isOwner = !callback.isOwner
                toggleShopping()
                setIcon()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
     * Check whether the app has the Manifest.permission.RECORD_AUDIO permission
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

    /**
     * Checks whether one is in shopping mode or not
     */
    private fun toggleShopping() {
        with(binding){
            isShopping = !callback.isOwner
        }
    }

    /**
     * Set the icon depending on whether one is in shopping mode
     */
    private fun setIcon() {
        val icon = if (!callback.isOwner) R.drawable.ic_cancel else R.drawable.ic_shopping_cart

        (myMenu.findItem(R.id.action_shop)).icon = ContextCompat.getDrawable(requireContext(), icon) as Drawable
    }

    companion object {
        private const val REQUEST_CODE = 1

        private val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO)
    }
}
