package com.yusufyildiz.experttalk.ui.signup

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
import com.yusufyildiz.experttalk.databinding.FragmentSignUpBinding
import com.yusufyildiz.experttalk.ui.SignInUiEvent
import com.yusufyildiz.experttalk.ui.SignUpUiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        val state = signUpViewModel.state
        with(binding){
            alreadyHaveAnAccountText.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }

            signUpButton.setOnClickListener {
                val name = signUpUserNameEditText.text.toString()
                val email = signUpEmailEditText.text.toString()
                val password = signUpPasswordEditText.text.toString()
                val passwordAgain = signUpPasswordAgainEditText.text.toString()
                val phone = signUpPhoneEditText.text.toString()

                if(checkFields(
                        name,
                        phone,
                        email,
                        password,
                        passwordAgain
                )){
                    if(password.trim() != passwordAgain.trim()){
                        Toast.makeText(context,"Şifreler uyuşmuyor",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        signUpEmailEditText.text.apply {
                            state.signUpEmail = this.toString()
                        }
                        signUpUserNameEditText.text.apply {
                            state.signUpUsername = this.toString()
                        }
                        signUpPasswordEditText.text.apply {
                            state.signUpPassword = this.toString()
                        }

                        signUpViewModel.onEvent(SignUpUiEvent.SignUp)
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
                    is AuthResult.Authorized -> {
                        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
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
}