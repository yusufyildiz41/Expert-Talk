package com.yusufyildiz.experttalk.ui.search.expert_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems
import com.yusufyildiz.experttalk.databinding.FragmentExpertDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpertDetailFragment : Fragment() {

    private lateinit var binding: FragmentExpertDetailBinding
    private val expertDetailViewModel: ExpertDetailViewModel by viewModels()
    private var expertDetailRecyclerViewAdapter = ExpertDetailRecyclerView(arrayListOf())
    var username : String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExpertDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            username = it.getString("username")
        }

        expertDetailViewModel.getExpertDetail(username!!)
        initCollectors()

        with(binding){
            expertDetailAboutCategoryButton.setOnClickListener {
                expertDetailAboutCategoryButton.setBackgroundResource(R.drawable.categorie_filled_bg)
                expertDetailFilesButton.setBackgroundResource(R.drawable.categorie_empty_bg)
                expertDetailAboutCategoryButton.setTextColor(resources.getColor(R.color.white))
                expertDetailFilesButton.setTextColor(resources.getColor(R.color.inputTextColor))
                aboutHolderLinearLayout.visibility = View.VISIBLE
                expertDetailFilesRecylcerView.visibility = View.GONE
            }
            expertDetailFilesButton.setOnClickListener {
                expertDetailFilesButton.setBackgroundResource(R.drawable.categorie_filled_bg)
                expertDetailAboutCategoryButton.setBackgroundResource(R.drawable.categorie_empty_bg)
                expertDetailFilesButton.setTextColor(resources.getColor(R.color.white))
                expertDetailAboutCategoryButton.setTextColor(resources.getColor(R.color.inputTextColor))
                aboutHolderLinearLayout.visibility = View.GONE
                expertDetailFilesRecylcerView.visibility = View.VISIBLE
            }
            searchDetailExpertAppointmentButton.setOnClickListener {
                val action = ExpertDetailFragmentDirections.actionExpertDetailFragmentToAppointmentFragment(username.toString())
                Navigation.findNavController(it).navigate(action)
            }

        }
    }

    private fun initCollectors(){
        lifecycleScope.launchWhenCreated {
            expertDetailViewModel.expertDetailStateFlow.collect{ expertRequest->
                with(binding){
                    searchDetailExpertUsernameTextView.text = expertRequest.username.toString()
                    searchDetailExpertPriceTextView.text = expertRequest.expertPrice.toString()
                    expertDetailJobTextView.text = expertRequest.expertCategory.toString()
                    searchDetailExpertAboutTextView.text = expertRequest.about.toString()
                    expertDetailSelfProfileTextView.text = expertRequest.longAbout.toString()
                    expertRequest.expertCategoryDetail?.let {
                        jobDetailRecyclerViewItems(it)
                    }

                }
            }
        }

        expertDetailViewModel.dataLoading.observe(viewLifecycleOwner){ loading->
            if(loading){
                showProgressBar()
            }else{
                binding.cardView.visibility = View.VISIBLE
                binding.aboutHolderLinearLayout.visibility = View.VISIBLE
                binding.dataLoadingProgressBar.visibility = View.GONE
            }
        }
    }

    private fun showProgressBar(){
        binding.dataLoadingProgressBar.visibility = View.VISIBLE
        binding.cardView.visibility = View.GONE
        binding.aboutHolderLinearLayout.visibility = View.GONE
    }

    private fun jobDetailRecyclerViewItems(categoryDetailItems: ArrayList<CategoryItems>){
        with(binding){
            expertJobDetailRecyclerView.adapter = expertDetailRecyclerViewAdapter
            expertDetailRecyclerViewAdapter.updateExpertCategoryDetailList(categoryDetailItems)
        }
    }
}