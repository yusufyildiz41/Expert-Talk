package com.yusufyildiz.experttalk.ui.search.expert_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems
import com.yusufyildiz.experttalk.databinding.ExpertCategorieDetailRecyclerRowBinding

class ExpertDetailRecyclerView(
    private val expertCategoryDetailList: ArrayList<CategoryItems>
): RecyclerView.Adapter<ExpertDetailRecyclerView.ExpertDetailViewHolder>() {

    class ExpertDetailViewHolder(val binding:ExpertCategorieDetailRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertDetailViewHolder {
        val binding = ExpertCategorieDetailRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpertDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpertDetailViewHolder, position: Int) {
        val item = expertCategoryDetailList[position]
        with(holder.binding){
            expertCategoryDetailTextView.text = item.categoryItem.toString()
        }
    }

    override fun getItemCount(): Int {
        return expertCategoryDetailList.size
    }

    fun updateExpertCategoryDetailList(newList: ArrayList<CategoryItems>){
        expertCategoryDetailList.clear()
        expertCategoryDetailList.addAll(newList)
        notifyDataSetChanged()
    }

}