package com.yusufyildiz.experttalk.ui.profile.expert

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
import com.yusufyildiz.experttalk.databinding.FragmentExpertProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.exp

@AndroidEntryPoint
class ExpertProfileFragment : Fragment() {

    private lateinit var binding: FragmentExpertProfileBinding
    private val expertProfileViewModel: ExpertProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpertProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("STATE", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("userEmail","").toString()
        password = sharedPreferences.getString("userPassword","").toString()

        with(binding) {
            expertProfileViewModel.getExpertData(email,password)
            initCollectors()
            signOutExpertProfileButton.setOnClickListener {
                expertProfileViewModel.signOut().also {
                    findNavController().navigate(R.id.action_expertProfileFragment_to_expertSignInFragment)
                }
            }
        }
    }

    private fun initCollectors(){
        lifecycleScope.launchWhenStarted {
            expertProfileViewModel.expertStateFlow.collect{ expertRequest ->
                with(binding){
                    usernameExpertProfileTextView.text = expertRequest.username
                    emailExpertProfileTextView.text = expertRequest.email
                    expertIdExpertProfileText.text = expertRequest.expertId
                    jobExpertProfileText.text = expertRequest.expertCategory
                    meetTimeExpertProfileText.text = expertRequest.expertTime
                    phoneNumberExpertProfileTextView.text = expertRequest.phone
                    priceExpertProfileText.text = expertRequest.expertPrice
                }
            }
        }
    }

}