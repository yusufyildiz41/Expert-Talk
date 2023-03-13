package com.yusufyildiz.experttalk.ui.profile.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.databinding.FragmentUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("STATE", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("userEmail","")!!
        password = sharedPreferences.getString("userPassword","")!!

        userProfileViewModel.getUserData(email,password)
        initCollectors()

        with(binding){
            signOutUserProfileButton.setOnClickListener {
                userProfileViewModel.signOut().also {
                    findNavController().navigate(R.id.action_userProfileFragment_to_signInFragment)
                }
            }
        }

    }

    private fun initCollectors(){
        lifecycleScope.launchWhenStarted {
            userProfileViewModel.userStateFlow.collect{
                with(binding){
                    usernameUserProfileTextView.text = it.username
                    phoneNumberUserProfileTextView.text = it.phone
                    emailUserProfileTextView.text = it.email
                }
            }
        }
    }
}