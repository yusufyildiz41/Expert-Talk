package com.yusufyildiz.experttalk.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.viewpager2.widget.ViewPager2
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.RecyclerViewItems
import com.yusufyildiz.experttalk.data.adapters.ViewPagerAdapter
import com.yusufyildiz.experttalk.data.model.IntroPageModel
import com.yusufyildiz.experttalk.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    private val viewPagerListAdapter = ViewPagerAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            getIntroViewPager()
            skipTextView.setOnClickListener {
                findNavController().navigate(R.id.action_introFragment_to_signInFragment)
            }
            nextButton.setOnClickListener {
                //TODO: The button may be dropped from here
                findNavController().navigate(R.id.action_introFragment_to_signInFragment)
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    with(binding) {
                        if (position == 0) {
                            firstPageCircle.setImageResource(R.drawable.circle_filled)
                            secondPageCircle.setImageResource(R.drawable.circle_empty)
                            thirdPageCircle.setImageResource(R.drawable.circle_empty)
                        } else if (position == 1) {
                            firstPageCircle.setImageResource(R.drawable.circle_empty)
                            secondPageCircle.setImageResource(R.drawable.circle_filled)
                            thirdPageCircle.setImageResource(R.drawable.circle_empty)
                        } else {
                            firstPageCircle.setImageResource(R.drawable.circle_empty)
                            secondPageCircle.setImageResource(R.drawable.circle_empty)
                            thirdPageCircle.setImageResource(R.drawable.circle_filled)
                            nextButton.visibility = View.VISIBLE

                        }
                    }
                }
            })
        }
    }

    private fun getIntroViewPager() {
        val introPageModelList = RecyclerViewItems.getIntroPageData()
        with(binding) {
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.beginFakeDrag()
            viewPager.fakeDragBy(10f)
            viewPager.endFakeDrag()
            viewPager.adapter = viewPagerListAdapter
            viewPagerListAdapter.updateItems(introPageModelList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding){
            viewPager.removeAllViews()
        }
    }

}