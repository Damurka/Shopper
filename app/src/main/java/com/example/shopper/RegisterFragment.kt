package com.example.shopper

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.shopper.databinding.FragmentRegisterBinding
import com.example.shopper.models.Profile


class RegisterFragment : UnauthenticatedFragment() {

    /**
     * Register Fragment binding view
     */
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // If login button clicked move to the Login fragment
        binding.login.setOnClickListener {
            navController.popBackStack(R.id.login_dest, false)
        }

        // When register button clicked create an account
        binding.registerButton.setOnClickListener {
            showProgressBar()

            if (binding.name.text.isNullOrBlank() ||
                binding.email.text.isNullOrBlank() ||
                binding.password.text.isNullOrBlank() ||
                binding.password.text.toString() != binding.confirmPassword.text.toString()) {
                viewModel.invalidAccountInfo()
            } else {
                val name = binding.name.text.toString()
                val phone = binding.phone.text.toString()
                val email = binding.email.text.toString()
                val profile = Profile(email= email, name = name, phone = phone)

                viewModel.createAccount(requireActivity() as Activity, binding.email.text.toString(), binding.password.text.toString(), profile)
            }
        }

        // When back button pressed cancel registration
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.userCancelledRegistration()
            navController.popBackStack(R.id.login_dest, false)
        }

        return binding.root
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.group.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.group.visibility = View.GONE
    }
}