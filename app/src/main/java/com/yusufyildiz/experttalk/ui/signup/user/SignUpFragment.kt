package com.yusufyildiz.experttalk.ui.signup.user

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
import com.yusufyildiz.experttalk.common.UserAuthResult
import com.yusufyildiz.experttalk.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            alreadyHaveAnAccountText.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }

            signUpButton.setOnClickListener {
                val username = signUpUserNameEditText.text.toString()
                val email = signUpEmailEditText.text.toString()
                val password = signUpPasswordEditText.text.toString()
                val passwordAgain = signUpPasswordAgainEditText.text.toString()
                val phone = signUpPhoneEditText.text.toString()

                if(checkFields(
                        username,
                        phone,
                        email,
                        password,
                        passwordAgain
                )){
                    if(password.trim() != passwordAgain.trim()){
                        Toast.makeText(context,"Şifreler uyuşmuyor",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        signUpViewModel.signUp(
                            email,
                            username,
                            password,
                            phone
                        )
                    }
                } else {
                    Toast.makeText(context,"Lütfen tüm alanları doldurun !!!",Toast.LENGTH_SHORT).show()
                }
            }
        }
        initObservers()
    }

    private fun initObservers()
    {
        lifecycleScope.launchWhenCreated {
            signUpViewModel.authResult.collect { authResult->
                when(authResult){
                    is UserAuthResult.UserAuthorized -> {
                        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                    }
                    is UserAuthResult.UserUnAuthorized -> {
                        Toast.makeText(context,"You are not authorized",Toast.LENGTH_SHORT).show()
                    }
                    is UserAuthResult.UserUnknownError -> {
                        Toast.makeText(context,"An unknown error occurred",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkFields(
        nameText: String,
        phone: String,
        email: String,
        password: String,
        passwordAgain: String
    ): Boolean{
        val check = when {
            nameText.trim().isEmpty() -> false
            password.trim().isEmpty() -> false
            passwordAgain.trim().isEmpty() -> false
            email.trim().isEmpty() -> false
            phone.trim().isEmpty() -> false
            else -> true
        }
        return check
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }
}