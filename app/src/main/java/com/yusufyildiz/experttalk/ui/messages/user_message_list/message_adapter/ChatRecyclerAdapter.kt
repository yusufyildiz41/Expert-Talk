package com.yusufyildiz.experttalk.ui.messages.user_message_list.message_adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.Utils
import com.yusufyildiz.experttalk.databinding.ChatReceiverRecyclerRowBinding
import com.yusufyildiz.experttalk.databinding.ChatSenderRecyclerRowBinding
import com.yusufyildiz.experttalk.domain.model.Message

class ChatRecyclerAdapter(
    private val activity: Activity,
    private val messages: ArrayList<Message>
): RecyclerView.Adapter<RecyclerView.ViewHolder?>(){


    private val ITEM_SENT = 1
    private val ITEM_RECEIVE = 2

    var userEmail = Utils.getCurrentUserEmail(activity)

    inner class SentMessageHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: ChatSenderRecyclerRowBinding = ChatSenderRecyclerRowBinding.bind(itemView)
    }

    inner class ReceiveMessageHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: ChatReceiverRecyclerRowBinding = ChatReceiverRecyclerRowBinding.bind(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if (messages.senderEmail == userEmail){
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Toast.makeText(activity,"data: $userEmail",Toast.LENGTH_SHORT).show()
        return if(viewType == ITEM_SENT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_sender_recycler_row,parent,false)
            SentMessageHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_receiver_recycler_row,parent,false)
            ReceiveMessageHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]

        if(holder.javaClass == SentMessageHolder::class.java){
            val viewHolder = holder as SentMessageHolder
            viewHolder.binding.chatSenderRecyclerTextView.text = message.text
            viewHolder.binding.chatSenderUserEmailText.text = message.senderEmail
        } else {
            val viewHolder = holder as ReceiveMessageHolder
            viewHolder.binding.chatReceiverRecyclerTextView.text = message.text
            viewHolder.binding.chatReceiverUserEmailTextView.text = message.senderEmail
        }
    }

    fun updateMessages(newList: ArrayList<Message>){
        messages.clear()
        messages.addAll(newList)
        notifyDataSetChanged()
    }
}