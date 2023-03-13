package com.yusufyildiz.experttalk.ui.messages.user_message_list.message_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.databinding.MessageListRecyclerRowBinding

class MessagesRecyclerAdapter(
    private val expertMessagesList: ArrayList<ExpertRequest>
): RecyclerView.Adapter<MessagesRecyclerAdapter.MessagesViewHolder>() {

    class MessagesViewHolder(val binding: MessageListRecyclerRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val binding = MessageListRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val item = expertMessagesList[position]
        with(holder.binding){
            messageExpertProfileImageView.setImageResource(R.drawable.engineer)
            messageExpertNameTextView.text = item.username.toString()
            messageListLinearLayout.setOnClickListener {
                val bundle = bundleOf("expertEmail" to item.email.toString())
                Navigation.findNavController(it).navigate(R.id.action_messagesFragment_to_expertMessagesFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return expertMessagesList.size
    }

    fun updateExpertMessageList(newList: ArrayList<ExpertRequest>){
        expertMessagesList.clear()
        expertMessagesList.addAll(newList)
        notifyDataSetChanged()
    }

}