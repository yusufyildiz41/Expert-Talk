package com.yusufyildiz.experttalk.listeners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DateAndTimeViewModel: ViewModel() {
    private val _dateStateFlow = MutableStateFlow("")
    val dateStateFlow = _dateStateFlow.asStateFlow()

    private val _timeStateFlow = MutableStateFlow("")
    val timeStateFlow = _timeStateFlow.asStateFlow()

    fun getDateAndTimeData(date:String, time: String){
        viewModelScope.launch {
            _dateStateFlow.value = date
            _timeStateFlow.value = time
        }
    }
}