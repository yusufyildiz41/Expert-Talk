package com.yusufyildiz.experttalk.listeners

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.CalendarView
import android.widget.Toast
import com.yusufyildiz.experttalk.databinding.CustomAppointmentAlertDialogBinding

class DateChangeListener(
    private val context: Context,
    private val dateAndTimeViewModel: DateAndTimeViewModel
    ) : CalendarView.OnDateChangeListener {

    private lateinit var binding: CustomAppointmentAlertDialogBinding
    var time: String? = ""
    var date: String? = ""

    override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, day: Int) {

        date = "$day.${month+1}.$year"
        showMessage("Date : $date")
        TimePickerDialog(context,TimePickerDialogListener(),24,60,true).show()
    }

    private fun TimePickerDialogListener(): TimePickerDialog.OnTimeSetListener? {
        return TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

            binding = CustomAppointmentAlertDialogBinding.inflate(LayoutInflater.from(context))
            time = "$hour . $minute "
            binding.hourInputTextView.text = time
            binding.dateInputTextView.text = date
            showMessage("Time = $time").also {

                var alert = AlertDialog.Builder(context)

                alert.setTitle("Randevu Bilgileriniz")
                alert.setView(binding.root)
                alert.setPositiveButton("Onayla",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        dateAndTimeViewModel.getDateAndTimeData(date.toString(),time.toString())
                        showMessage("Randevu tarihi ve saati onaylanmıştır")
                    }
                })
                alert.setNegativeButton("Vazgeç",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        showMessage("Randevu talebiniz iptal edilmiştir")
                    }
                })
                alert.show()
            }
        }
    }

    fun showMessage(message: String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}