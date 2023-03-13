package com.yusufyildiz.experttalk.data.model.appointment


data class AppointmentRequest(
    var expertEmail: String?=null,
    var userEmail: String?=null,
    var date: String?=null,
    var time: String?=null
)