package com.yusufyildiz.experttalk.ui.home.home_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.data.model.recycler_view.ExpertWorkedRecyclerModel
import com.yusufyildiz.experttalk.databinding.WorkedExpertRecyclerRowBinding

class ExpertWorkedRecyclerAdapter(
  private val expertWorkedList: ArrayList<ExpertWorkedRecyclerModel>
) : RecyclerView.Adapter<ExpertWorkedRecyclerAdapter.WorkedViewHolder>() {

    class WorkedViewHolder(val binding: WorkedExpertRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkedViewHolder {
        val binding = WorkedExpertRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WorkedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkedViewHolder, position: Int) {
        val item = expertWorkedList[position]

        with(holder.binding){
            workedExpertImageView.setImageResource(item.expertWorkedImage!!)
            workedExpertJobTextView.text = item.expertWorkedJob.toString()
            workedExpertNameTextView.text = item.expertWorkedName.toString()
            workedExpertRatingBar.rating = item.expertWorkedStarRate.toString().toFloat()
        }
    }

    override fun getItemCount(): Int {
        return expertWorkedList.size
    }

    fun updateExpertWorkedList(newList: ArrayList<ExpertWorkedRecyclerModel>){
        expertWorkedList.clear()
        expertWorkedList.addAll(newList)
        notifyDataSetChanged()
    }

}