package com.yusufyildiz.experttalk.ui.signupexpert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout.LayoutParams
import android.widget.Toast
import androidx.core.view.get
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.ExpertCategories
import com.yusufyildiz.experttalk.databinding.FragmentSignUpExpertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpExpertFragment : Fragment() {

    private lateinit var binding: FragmentSignUpExpertBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpExpertBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            val categoryAdapterItems = ArrayAdapter(requireContext(),R.layout.category_list_item,getCategoryItems())
            categorySelectText.setAdapter(categoryAdapterItems)

            categorySelectText.setOnItemClickListener { adapterView, view, i, l ->
                when(adapterView.getItemAtPosition(i)){
                    "Avukat"-> {
                        subCategoryView.removeAllViews()
                        val inflater = LayoutInflater.from(requireContext()).inflate(R.layout.lawyer_items,null)
                        val view = inflater.rootView
                        subCategoryView.addView(view)
                    }
                    "Dil Pratiği" -> {
                        subCategoryView.removeAllViews()
                        val inflater = LayoutInflater.from(requireContext()).inflate(R.layout.language_practice_items,null)
                        val view = inflater.rootView
                        subCategoryView.addView(view)
                    }
                    "Yaşam Koçu" -> {
                        subCategoryView.removeAllViews()
                        val inflater = LayoutInflater.from(requireContext()).inflate(R.layout.coach_items_layout,null)
                        val view = inflater.rootView
                        subCategoryView.addView(view)
                    }
                    "E-Ticaret Uzmanı"->{
                        subCategoryView.removeAllViews()
                        val inflater = LayoutInflater.from(requireContext()).inflate(R.layout.e_commerce_items,null)
                        val view = inflater.rootView
                        subCategoryView.addView(view)
                    } else -> {
                        subCategoryView.removeAllViews()
                    }
                }
            }
        }
    }

    private fun getCategoryItems(): ArrayList<String>{
        val expertCategoryList = ExpertCategories.getExpertCategories()
        return expertCategoryList
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

}