package com.yusufyildiz.experttalk.ui.signup.expert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.databinding.FileItemRecyclerRowBinding

class FileNamesRecyclerAdapter(val fileNameList: ArrayList<String>): RecyclerView.Adapter<FileNamesRecyclerAdapter.FileNameViewHolder>() {

    class FileNameViewHolder(val binding: FileItemRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileNameViewHolder {
        val binding = FileItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FileNameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileNameViewHolder, position: Int) {
        var item = fileNameList[position]
        holder.binding.fileNameTextView.text = item
    }

    override fun getItemCount(): Int {
        return fileNameList.size
    }

    fun updateFileList(newList: ArrayList<String>){
        fileNameList.clear()
        fileNameList.addAll(newList)
        notifyDataSetChanged()
    }


}