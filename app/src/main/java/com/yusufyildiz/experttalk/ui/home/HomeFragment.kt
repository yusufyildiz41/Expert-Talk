package com.yusufyildiz.experttalk.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufyildiz.experttalk.common.RecyclerViewItems
import com.yusufyildiz.experttalk.ui.home.home_adapter.ExpertCategoriesRecyclerAdapter
import com.yusufyildiz.experttalk.ui.home.home_adapter.ExpertRecyclerAdapter
import com.yusufyildiz.experttalk.ui.home.home_adapter.ExpertWorkedRecyclerAdapter
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var expertWorkedRecyclerAdapter = ExpertWorkedRecyclerAdapter(arrayListOf())
    private var expertPersonsRecyclerAdapter = ExpertRecyclerAdapter(arrayListOf())
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding){
            homeViewModel.getAllExpertsData()
            initCollectors()
            expertCategoriesRecyclerView()
            expertWorkedRecyclerView()

            searchExpertEditText.setOnTouchListener { view, motionEvent ->
                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        val direction = HomeFragmentDirections.actionHomeFragmentToExpertSearchFragment()
                        Navigation.findNavController(view).navigate(direction)
                        true
                    } else -> false
                }
            }
//            searchExpertEditText.setOnTouchListener { p0, p1 ->
//
//                true
//            }


        }
    }

    private fun initCollectors(){
        lifecycleScope.launchWhenCreated {
            homeViewModel.expertsStateFlow.collect{ experts ->
                expertPersonsRecyclerView(experts)
            }
        }
        homeViewModel.dataLoading.observe(viewLifecycleOwner){ loading ->
            if(loading){
                binding.expertDataLoading.visibility = View.VISIBLE
                binding.expertPersonsRecyclerView.visibility = View.GONE
            } else {
                binding.expertDataLoading.visibility = View.GONE
                binding.expertPersonsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun expertCategoriesRecyclerView(){
        val expertCategoriesList = RecyclerViewItems.getExpertCategoriesData()
        ExpertCategoriesRecyclerAdapter().apply {
            binding.expertCategoryRecyclerView.adapter = this
            updateCategoriesList(expertCategoriesList)
        }
    }

    private fun expertWorkedRecyclerView(){
        val expertWorkedList = RecyclerViewItems.getWorkedExpertData()
        with(binding){
            workedExpertPersonsRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            workedExpertPersonsRecyclerView.adapter = expertWorkedRecyclerAdapter
            expertWorkedRecyclerAdapter.updateExpertWorkedList(expertWorkedList)
        }
    }

    private fun expertPersonsRecyclerView(expertsList: ArrayList<ExpertRequest>){
        //val expertPersonsList = RecyclerViewItems.getExpertData()
        with(binding){
            expertPersonsRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            expertPersonsRecyclerView.adapter = expertPersonsRecyclerAdapter
            expertPersonsRecyclerAdapter.updateExpertWorkedList(expertsList)
        }
    }

}