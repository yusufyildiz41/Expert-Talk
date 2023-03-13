package com.yusufyildiz.experttalk.ui.messages.user_message_list.expertmessages

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
import com.yusufyildiz.experttalk.ui.messages.user_message_list.message_adapter.ChatRecyclerAdapter
import com.yusufyildiz.experttalk.databinding.FragmentExpertMessagesBinding
import com.yusufyildiz.experttalk.domain.model.Message
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpertMessagesFragment : Fragment() {

    private lateinit var binding: FragmentExpertMessagesBinding
    private val expertMessagesViewModel: ExpertMessagesViewModel by viewModels()
    private lateinit var chatAdapter: ChatRecyclerAdapter
    private var chatList: ArrayList<Message>?= arrayListOf()
    private var expertEmail = ""
    private var userState = ""
    private var userEmail = ""

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpertMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatRecyclerAdapter(requireActivity(), arrayListOf())
        sharedPreferences = requireActivity().getSharedPreferences("STATE",Context.MODE_PRIVATE)

        userState = sharedPreferences.getString("state","").toString()
        userEmail = sharedPreferences.getString("userEmail","").toString()

        arguments?.let {
            expertEmail = it.getString("expertEmail").toString()
        }

        showMessage("expert messages user state :$userState")
        showMessage("user email is : $userEmail")

        expertMessagesViewModel.addSession(userEmail,expertEmail)
        with(binding) {
            val chatText = chatText.text
           // expertMessagesViewModel.observeMessages()
            initMessageCollector()
            sendButtonImage.setOnClickListener {
                showMessage("clicked")
                expertMessagesViewModel.sendMessage(chatText.toString())
                expertMessagesViewModel.observeMessages()
                chatText.clear()
            }
        }
    }

    private fun initMessageCollector() {

        lifecycleScope.launchWhenStarted {
            expertMessagesViewModel.state.collectLatest {
                chatRecyclerView(it.messages)
            }
        }
    }

        /*
        lifecycleScope.launchWhenCreated {
            expertMessagesViewModel.dataLoading.collect { loading ->
                binding.progressBar.isVisible = loading
            }
        }

         */

    private fun chatRecyclerView(messageList: ArrayList<Message>) {
        with(binding) {
            chatRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            chatAdapter.updateMessages(messageList)
            chatRecyclerView.adapter = chatAdapter

        }

    }

    fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                expertMessagesViewModel.disconnect()
                showMessage("back pressed was called")
                findNavController().navigate(R.id.messagesFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callBack)
    }
}