package com.example.shopper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.shopper.databinding.FragmentLoginBinding


class LoginFragment : UnauthenticatedFragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.group.visibility = View.GONE

            if (binding.email.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                viewModel.invalidAccountInfo()
            } else {
                viewModel.authenticate(requireActivity(), binding.email.text.toString(), binding.password.text.toString())
            }
        }

        binding.registerButton.setOnClickListener {
            navController.navigate(R.id.action_login_dest_to_register_dest)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
            navController.popBackStack(R.id.home_dest, false)
        }

        return binding.root
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.group.visibility = View.VISIBLE
    }

}
