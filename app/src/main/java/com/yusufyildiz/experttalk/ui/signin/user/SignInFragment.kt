package com.yusufyildiz.experttalk.ui.signin.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.UserAuthResult
import com.yusufyildiz.experttalk.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val signInViewModel : SignInViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = context?.getSharedPreferences("STATE", Context.MODE_PRIVATE)!!
        initObservers()

        with(binding) {

            signInButton.setOnClickListener {
                val email = signInEmailEditText.text.toString()
                val password = signInPasswordText.text.toString()

                if(checkFields(
                        email,
                        password
                )){
                    signInViewModel.signIn(
                        email,
                        password
                    ).also {
                        addSharedPreferences(email,password)
                    }

                }else {
                    showToast("Boş bırakılan yerler var")
                }
            }

            videoCallingButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_videoCallingFragment)
            }

            signUpPageTextView.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            signUpExpertPageText.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_expertSignInFragment)
            }

        }
    }

    private fun initObservers()
    {
        lifecycleScope.launchWhenCreated {
            signInViewModel.authResult.collect {result ->
                when(result) {
                    is UserAuthResult.UserAuthorized -> {
                        findNavController().navigate(R.id.homeFragment)
                        showToast("UserAuthorized")
                    }
                    is UserAuthResult.UserUnAuthorized -> {
                        removeSharedPreferences()
                        showToast("UserUnAuthorized")
                    }
                    is UserAuthResult.UserUnknownError -> {
                        removeSharedPreferences()
                        showToast("UserUnknownError")
                    }
                }
            }
        }

        signInViewModel.resultLoading.observe(viewLifecycleOwner){ loading->
            binding.progressBar.isVisible = loading
        }
    }

    private fun addSharedPreferences(email: String,password: String){
        val editor = sharedPreferences.edit()
        editor.putString("state","user")
        editor.putString("userEmail",email)
        editor.putString("userPassword",password)
        editor.commit()
    }
    private fun removeSharedPreferences(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    private fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun checkFields( //viewmodel
        email: String,
        password: String
    ): Boolean {
        val check = when {
            email.trim().isEmpty() -> false
            password.trim().isEmpty() -> false
            else -> true
        }
        return check
    }

}