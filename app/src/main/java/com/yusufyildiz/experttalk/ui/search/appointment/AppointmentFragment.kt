package com.yusufyildiz.experttalk.ui.search.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yusufyildiz.experttalk.data.model.appointment.AppointmentRequest
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.listeners.DateAndTimeViewModel
import com.yusufyildiz.experttalk.listeners.DateChangeListener
import com.yusufyildiz.experttalk.databinding.FragmentAppointmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AppointmentFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private val dateAndTimeViewModel: DateAndTimeViewModel by viewModels()
    var username: String? =""
    var expert: ExpertRequest?=null
    private var dateData: String?=""
    private var timeData: String?=""
    private var appointmentList: ArrayList<AppointmentRequest>?= arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            username = bundle.getString("username")
        }

        with(binding){
            appointmentViewModel.getExpertData(username.toString())
            initCollectors()

            appointmentNextButton.setOnClickListener {
                if(expert!=null){
                    val appointmentList = createAppointment(expertEmail = expert!!.email.toString())
                    appointmentViewModel.addAppointment(appointmentList!!)
                    showMessage("Randevunuz başarılı bir şekilde sisteme kaydedilmiştir")
                } else {
                    showMessage("Lütfen bir uzman seçin")
                }
            }
            calenderView.setOnDateChangeListener(DateChangeListener(requireContext(),dateAndTimeViewModel)).also {
                appointmentNextButton.visibility = View.VISIBLE
            }
        }
    }

    private fun initCollectors(){
        lifecycleScope.launchWhenCreated {
            appointmentViewModel.expertDetailStateFlow.collect{ expertRequest ->
                with(binding){
                    expert= expertRequest
                    searchAppointmentExpertAboutTextView.text = expertRequest.about.toString()
                    searchAppointmentExpertPriceTextView.text = expertRequest.expertPrice.toString()
                    searchAppointmentExpertUsernameTextView.text = expertRequest.username.toString()
                }
            }
        }
        appointmentViewModel.dataLoading.observe(viewLifecycleOwner){ loading ->
            if(loading){
                with(binding){
                    dataLoadingProgressBar.visibility = View.VISIBLE
                    cardViewAppointment.visibility = View.GONE
                    makeAnAppointment.visibility = View.GONE
                    calenderView.visibility = View.GONE
                    appointmentNextButton.visibility = View.GONE
                }
            }else {
                with(binding){
                    dataLoadingProgressBar.visibility = View.GONE
                    cardViewAppointment.visibility = View.VISIBLE
                    makeAnAppointment.visibility = View.VISIBLE
                    calenderView.visibility = View.VISIBLE
                    appointmentNextButton.visibility = View.VISIBLE
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            dateAndTimeViewModel.dateStateFlow.collect{ date->
                dateData = date
            }
        }
        lifecycleScope.launchWhenCreated {
            dateAndTimeViewModel.timeStateFlow.collect{ time->
                timeData = time
            }
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    private fun createAppointment(expertEmail: String): ArrayList<AppointmentRequest>?{
        val appointment = AppointmentRequest(
            expertEmail,
            "yusuf123123@gmail.com",
            dateData.toString(),
            timeData.toString()
        )
        appointmentList?.add(appointment)
        return appointmentList
    }
}