package com.yusufyildiz.experttalk.ui.signin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.auth.AuthResult
import com.yusufyildiz.experttalk.databinding.FragmentSignInBinding
import com.yusufyildiz.experttalk.ui.SignInUiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val signInViewModel : SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            val state = signInViewModel.state

            initObservers()

            val email = signInEmailEditText.text
            val username = signInEmailEditText.text
            val password = signInPasswordText.text
            state.signInEmail = email.toString()
            state.signInUsername = username.toString()
            state.signInPassword = password.toString()

            signInButton.setOnClickListener {
               signInViewModel.onEvent(SignInUiEvent.SignIn)
            }

            videoCallingButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_videoCallingFragment)
            }

            signUpPageTextView.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            signUpExpertPageText.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpExpertFragment)
            }

        }
    }

    private fun initObservers()
    {
        lifecycleScope.launchWhenCreated {
            signInViewModel.authResult.collect {result ->
                when(result) {
                    is AuthResult.Authorized -> {
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    }
                    is AuthResult.Unauthorized -> {
                        Toast.makeText(context,"You are not authorized",Toast.LENGTH_SHORT).show()
                    }
                    is AuthResult.UnknownError -> {
                        Toast.makeText(context,"An unknown error occurred",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}