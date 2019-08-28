package com.example.shopper

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopper.databinding.FragmentHomeBinding


/**
 * HomeFragment
 *
 * The parent fragment to all views in the bottom navigation bar
 * ie ShoppingLisr, History, Notification and Search
 */
class HomeFragment : AuthenticatedFragment() {

    /**
     * NavController for the nav_graph_home
     */
    private lateinit var childNavController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup ActionBar
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.setupActionBarWithNavController(navController)

        // Get nested navController
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        childNavController = navHostFragment.navController

        // Setup BottomNavigation
        binding.bottomNavigation.setupWithNavController(navController = childNavController)

        // Change the fragment title according the fragment's title
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
        }

        // Handle back presses for the childNavController
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            childNavController.navigateUp() || navController.navigateUp()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                childNavController.navigateUp()
                true
            }
            R.id.action_profile -> {
                navController.navigate(R.id.action_home_dest_to_profile_dest)
                true
            }
            R.id.action_logout -> {
                authViewModel.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
