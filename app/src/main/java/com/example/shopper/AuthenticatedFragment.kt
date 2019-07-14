package com.example.shopper

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopper.viewmodels.AuthViewModel


open class AuthenticatedFragment : Fragment() {

    protected val authViewModel: AuthViewModel by activityViewModels()

    protected val navController get() = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    onAuthenticated()
                }
                else -> {
                    navController.navigate(R.id.login_dest)
                }
            }
        })
    }

    protected open fun onAuthenticated() {}
}