package com.yusufyildiz.experttalk.ui.signin.expert

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.ExpertAuthResult
import com.yusufyildiz.experttalk.databinding.FragmentExpertSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.utils.io.concurrent.*

@AndroidEntryPoint
class ExpertSignInFragment : Fragment() {

    private lateinit var binding: FragmentExpertSignInBinding
    private val expertSignInViewModel: ExpertSignInViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExpertSignInBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = context?.getSharedPreferences("STATE", Context.MODE_PRIVATE)!!

        initObservers()

        with(binding) {
            signInExpertButton.setOnClickListener {
                val email = signInExpertEditText.text.toString()
                val password = signInExpertPasswordEditText.text.toString()

                if (checkFields(email, password)){
                    expertSignInViewModel.signIn(email, password).also {
                        addSharedPreferences(email,password)
                    }
                }
                else showMessage("Lütfen tüm boşlukları doldurun")
            }

            signUpUserSignInPageTextView.setOnClickListener {
                findNavController().navigate(R.id.action_expertSignInFragment_to_signInFragment)
            }

            signUpExpertPageTextView.setOnClickListener {
                findNavController().navigate(R.id.action_expertSignInFragment_to_signUpExpertFragment)
            }
        }
    }

    //infix
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenCreated {
            expertSignInViewModel.authResult.collect { authResult ->
                when (authResult) {
                    is ExpertAuthResult.ExpertAuthorized -> {
                        showProgressBar()
                        findNavController().navigate(R.id.homeFragment)
                        showMessage("ExpertAuthorized")
                    }
                    is ExpertAuthResult.ExpertUnAuthorized -> {
                        clearSharedPreferences()
                        showMessage("ExpertUnAuthorized")
                    }
                    is ExpertAuthResult.ExpertUnknownError -> {
                        clearSharedPreferences()
                        showMessage("ExpertUnknownError")
                    }
                }
            }
        }

        expertSignInViewModel.resultLoading.observe(viewLifecycleOwner){loading ->
            if(loading){
                showProgressBar()
            }else {
                showItems()
            }
        }

    }

    private fun addSharedPreferences(email: String,password: String){
        val editor = sharedPreferences.edit()
        editor.putString("state","expert")
        editor.putString("userEmail",email)
        editor.putString("userPassword",password)
        editor.commit()
    }

    private fun clearSharedPreferences(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    //viewmodel
    private fun checkFields(
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

    private fun showItems(){
        with(binding){
            signInExpertButton.visibility = View.VISIBLE
            signInExpertEditText.visibility = View.VISIBLE
            signInExpertForgetPassword.visibility = View.VISIBLE
            signInExpertPasswordEditText.visibility = View.VISIBLE
            textView4.visibility = View.VISIBLE
            textView5.visibility = View.VISIBLE
            signUpExpertPageTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun showProgressBar(){
        with(binding){//progress
            progressBar.visibility = View.VISIBLE
            signInExpertButton.visibility = View.GONE
            signInExpertEditText.visibility = View.GONE
            signInExpertForgetPassword.visibility = View.GONE
            signInExpertPasswordEditText.visibility = View.GONE
            textView4.visibility = View.GONE
            textView5.visibility = View.GONE
            signUpExpertPageTextView.visibility = View.GONE
        }
    }
}