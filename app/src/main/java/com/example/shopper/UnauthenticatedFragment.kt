package com.example.shopper

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopper.viewmodels.AuthViewModel


open class UnauthenticatedFragment : Fragment() {

    protected val viewModel: AuthViewModel by activityViewModels()

    protected val navController get() = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    navController.popBackStack()
                }
                AuthViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                    Toast.makeText(requireActivity(),
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT).show()
                    viewModel.refuseAuthentication()
                }
                AuthViewModel.AuthenticationState.UNVERIFIED_ACCOUNT -> {
                    Toast.makeText(requireActivity(),
                        "Account is not verified. Please Check you email to verify your account",
                        Toast.LENGTH_LONG).show()
                    viewModel.refuseAuthentication()
                }
                AuthViewModel.AuthenticationState.REGISTRATION_COMPLETED -> {
                    navController.popBackStack(R.id.home_dest, false)
                    Toast.makeText(requireActivity(),
                        "Account successfully created",
                        Toast.LENGTH_SHORT).show()
                    viewModel.refuseAuthentication()
                }
                AuthViewModel.AuthenticationState.REGISTRATION_CANCELLED -> {
                    Toast.makeText(requireActivity(),
                        "Registration Process Cancelled",
                        Toast.LENGTH_SHORT).show()
                    viewModel.refuseAuthentication()
                }
                AuthViewModel.AuthenticationState.INVALID_ACCOUNT_INFO -> {
                    Toast.makeText(requireActivity(),
                        "Some of the information is invalid. Check the information and try again",
                        Toast.LENGTH_SHORT).show()
                    viewModel.refuseAuthentication()
                }
                else -> Log.d("UnauthenticatedFragment", "Stateless")
            }

            hideProgressBar()
        })
    }

    protected open fun hideProgressBar() { }
}