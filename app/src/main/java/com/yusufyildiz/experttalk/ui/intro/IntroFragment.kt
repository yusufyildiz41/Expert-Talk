package com.yusufyildiz.experttalk.ui.intro

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.RecyclerViewItems
import com.yusufyildiz.experttalk.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding //view binding delegate
    private val viewPagerListAdapter = ViewPagerAdapter(arrayListOf())
    private lateinit var sharedPreferences: SharedPreferences

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

        sharedPreferences = requireContext().getSharedPreferences("STATE", Context.MODE_PRIVATE)

        val state = sharedPreferences.getString("state",null)

        with(binding) {
            getIntroViewPager()
            skipTextView.setOnClickListener {
                when(state){
                    "user"-> {
                        findNavController().navigate(R.id.action_introFragment_to_signInFragment)
                    }
                    "expert"-> {
                        findNavController().navigate(R.id.expertSignInFragment)
                    } else -> findNavController().navigate(R.id.action_introFragment_to_signInFragment)
                }
            }
            nextButton.setOnClickListener {
                when(state){
                    "user"-> {
                        findNavController().navigate(R.id.action_introFragment_to_signInFragment)
                    }
                    "expert"-> {
                        findNavController().navigate(R.id.expertSignInFragment)
                    } else ->  findNavController().navigate(R.id.action_introFragment_to_signInFragment)
                }
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    with(binding) {
                        when (position) {
                            0 -> {
                                firstPageCircle.setImageResource(R.drawable.circle_filled)
                                secondPageCircle.setImageResource(R.drawable.circle_empty)
                                thirdPageCircle.setImageResource(R.drawable.circle_empty)
                            }
                            1 -> {
                                firstPageCircle.setImageResource(R.drawable.circle_empty)
                                secondPageCircle.setImageResource(R.drawable.circle_filled)
                                thirdPageCircle.setImageResource(R.drawable.circle_empty)
                            }
                            else -> {
                                firstPageCircle.setImageResource(R.drawable.circle_empty)
                                secondPageCircle.setImageResource(R.drawable.circle_empty)
                                thirdPageCircle.setImageResource(R.drawable.circle_filled)
                                nextButton.visibility = View.VISIBLE

                            }
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