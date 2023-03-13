package com.yusufyildiz.experttalk.ui.home.home_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.databinding.ExpertPersonsRecyclerRowBinding

class ExpertRecyclerAdapter(
    private val expertList: ArrayList<ExpertRequest>
): RecyclerView.Adapter<ExpertRecyclerAdapter.ExpertPersonsViewHolder>()  {

    class ExpertPersonsViewHolder(val binding: ExpertPersonsRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertPersonsViewHolder {
        val binding = ExpertPersonsRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpertPersonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpertPersonsViewHolder, position: Int) {

        val item = expertList[position]

        with(holder.binding){
            expertImageView.setImageResource(R.drawable.doctor)
            expertPriceTextView.text = item.expertPrice.toString()
            expertNameTextView.text = item.username.toString()
            expertStarRateTextView.text = "4"
        }
    }

    override fun getItemCount(): Int {
        return expertList.size
    }


    fun updateExpertWorkedList(newList: ArrayList<ExpertRequest>){
        expertList.clear()
        expertList.addAll(newList)
        notifyDataSetChanged()
    }


}