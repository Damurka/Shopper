package com.example.shopper


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.shopper.databinding.FragmentSplashBinding
import com.example.shopper.viewmodels.AuthViewModel


/**
 * SplashFragment
 *
 * The first fragment to be called used to check if the user is logged in
 * and redirects to the right screen login or the home fragment
 */
class SplashFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)

        val navController = findNavController()
        authViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                   navController.navigate(R.id.action_splash_dest_to_home_dest)
                }
                else -> {
                    navController.navigate(R.id.action_splash_dest_to_login_dest)
                }
            }
        })

        return binding.root
    }


}
