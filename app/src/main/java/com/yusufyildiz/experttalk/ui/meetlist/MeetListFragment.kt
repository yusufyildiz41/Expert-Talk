package com.yusufyildiz.experttalk.ui.meetlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.databinding.FragmentMeetListBinding


class MeetListFragment : Fragment() {

    private lateinit var binding: FragmentMeetListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeetListBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}