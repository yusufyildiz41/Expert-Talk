package com.yusufyildiz.experttalk.ui.messages.expert_message_list.user_messages

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.databinding.FragmentUserMessagesBinding
import com.yusufyildiz.experttalk.domain.model.Message
import com.yusufyildiz.experttalk.ui.messages.user_message_list.message_adapter.ChatRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserMessagesFragment : Fragment() {

    private lateinit var binding: FragmentUserMessagesBinding
    private val userMessagesViewModel: UserMessagesViewModel by viewModels()
    private lateinit var chatAdapter: ChatRecyclerAdapter
    private var expertEmail = ""
    private var userEmail = ""
    private var userState = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserMessagesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            sharedPreferences = requireActivity().getSharedPreferences("STATE",Context.MODE_PRIVATE)

            userState = sharedPreferences.getString("state","").toString()
            userEmail = sharedPreferences.getString("userEmail","").toString()

            userMessagesViewModel.addSession(userEmail,"ahmet@gmail.com")

            showMessage("expert messages user state :$userState")
            showMessage("user email is : $userEmail")
            val chatText = chatText.text
            initMessageCollector()
            // expertMessagesViewModel.observeMessages()
            sendButtonImage.setOnClickListener {
                showMessage("clicked")
                userMessagesViewModel.sendMessage(chatText.toString())
                userMessagesViewModel.observeMessages()
                chatText.clear()
            }
        }
    }
    private fun initMessageCollector(){
        lifecycleScope.launchWhenStarted {
            userMessagesViewModel.state.collectLatest {
                chatRecyclerView(it.messages)
            }

        }
//        lifecycleScope.launch {
//            userMessagesViewModel.messagesStateFlow
//                .flowWithLifecycle(lifecycle,Lifecycle.State.CREATED)
//                .collect { messageList ->
//                    chatRecyclerView(messageList)
//                }
//        }
    }

    private fun chatRecyclerView(messageList: ArrayList<Message>) {
        with(binding) {
            chatAdapter = ChatRecyclerAdapter(requireActivity(), arrayListOf())
            chatRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            chatAdapter.updateMessages(messageList)
            chatRecyclerView.adapter = chatAdapter

        }

    }

    private fun showMessage(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                userMessagesViewModel.disconnect()
                showMessage("back pressed was called")
                findNavController().navigate(R.id.expertMessageListFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callBack)
    }
}