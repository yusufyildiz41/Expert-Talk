package com.yusufyildiz.experttalk.ui.messages.user_message_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufyildiz.experttalk.ui.messages.user_message_list.message_adapter.MessagesRecyclerAdapter
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.databinding.FragmentMessagesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private val messagesRecyclerAdapter = MessagesRecyclerAdapter(arrayListOf())
    private val messagesViewModel : MessagesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMessagesBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            messagesViewModel.getExperts()
            initCollectors()
        }
    }

    private fun messagesExpertsRecyclerView(expertList: ArrayList<ExpertRequest>){

        with(binding){
            expertMessagesListRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            expertMessagesListRecyclerView.adapter = messagesRecyclerAdapter
            messagesRecyclerAdapter.updateExpertMessageList(expertList)
        }
    }

    private fun initCollectors(){
        lifecycleScope.launchWhenCreated {
            messagesViewModel.expertStateFlow.collect{ expertList ->
                messagesExpertsRecyclerView(expertList)
            }
        }
    }
}