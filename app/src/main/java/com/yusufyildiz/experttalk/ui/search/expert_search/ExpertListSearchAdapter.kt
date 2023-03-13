package com.yusufyildiz.experttalk.ui.search.expert_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.databinding.ExpertSearchListRecyclerRowBinding

class ExpertListSearchAdapter(
    private val expertList: ArrayList<ExpertRequest>
) : RecyclerView.Adapter<ExpertListSearchAdapter.ExpertListSearchViewHolder>() {

    class ExpertListSearchViewHolder(val binding: ExpertSearchListRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertListSearchViewHolder {
        val binding =
            ExpertSearchListRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpertListSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpertListSearchViewHolder, position: Int) {

        var item = expertList[position]

        with(holder.binding){
            searchListExpertUsernameTextView.text = item.username.toString()
            searchListExpertPriceTextView.text = item.expertPrice.toString()
            searchListExpertAboutTextView.text = item.about.toString()
            searchListExpertAppointmentButton.setOnClickListener {
                val bundle = bundleOf("username" to item.username.toString())
                Navigation.findNavController(it).navigate(R.id.action_expertSearchFragment_to_expertDetailFragment,bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return expertList.size
    }

    fun updateExpertList(newList: ArrayList<ExpertRequest>) {
        expertList.clear()
        expertList.addAll(newList)
        notifyDataSetChanged()
    }

}