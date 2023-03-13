package com.yusufyildiz.experttalk.ui.messages.expert_message_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.databinding.FragmentExpertMessageListBinding

class ExpertMessageListFragment : Fragment() {

    private lateinit var binding: FragmentExpertMessageListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpertMessageListBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            expertMessageText.setOnClickListener {
                findNavController().navigate(R.id.action_expertMessageListFragment_to_userMessagesFragment)
            }
        }
    }
}