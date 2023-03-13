package com.yusufyildiz.experttalk.ui.home.home_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.data.model.recycler_view.ExpertCategoriesRecyclerModel
import com.yusufyildiz.experttalk.databinding.ExpertCategorieRowBinding

class ExpertCategoriesRecyclerAdapter : RecyclerView.Adapter<ExpertCategoriesRecyclerAdapter.CategoriesViewHolder>() {

    private val expertCategoriesList = arrayListOf<ExpertCategoriesRecyclerModel>()

    class CategoriesViewHolder(val binding: ExpertCategorieRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = ExpertCategorieRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = expertCategoriesList[position]

        with(holder.binding){
            expertCategoriesImageView.setImageResource(item.categoriesImage!!)
            expertCategoriesNameTextView.text = item.categoriesName.toString()
        }

    }

    override fun getItemCount(): Int {
        return expertCategoriesList.size
    }

    fun updateCategoriesList(newList: ArrayList<ExpertCategoriesRecyclerModel>){
        expertCategoriesList.clear()
        expertCategoriesList.addAll(newList)
        notifyDataSetChanged()
    }
}