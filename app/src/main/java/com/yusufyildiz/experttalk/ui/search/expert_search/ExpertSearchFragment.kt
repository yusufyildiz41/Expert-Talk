package com.yusufyildiz.experttalk.ui.search.expert_search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.databinding.FragmentExpertSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpertSearchFragment : Fragment() {

    private lateinit var binding: FragmentExpertSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private var expertListRecyclerAdapter = ExpertListSearchAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExpertSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            searchViewModel.getExperts()
            initCollectors()
        }

    }

    private fun initCollectors(){
        lifecycleScope.launchWhenCreated {
            searchViewModel.expertStateFlow.collect{ expertList ->
                expertListRecyclerView(expertList)
            }
        }
        searchViewModel.dataLoading.observe(viewLifecycleOwner){ loading->
            if(loading){
                binding.expertsLoadingProgressBar.visibility = View.VISIBLE
                binding.expertListExpertSearchPageRecyclerView.visibility = View.GONE
            }
            else{
                binding.expertsLoadingProgressBar.visibility = View.GONE
                binding.expertListExpertSearchPageRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun expertListRecyclerView(expertList: ArrayList<ExpertRequest>){
        with(binding){
            expertListExpertSearchPageRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            expertListExpertSearchPageRecyclerView.adapter = expertListRecyclerAdapter
            expertListRecyclerAdapter.updateExpertList(expertList)
        }
    }

}