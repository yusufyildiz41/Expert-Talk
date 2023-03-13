package com.yusufyildiz.experttalk.common

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

object Utils {

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    const val BASE_URL = "http://192.168.1.132:8081"
    const val WEBSOCKET_URL = "ws://192.168.1.132:8081"
    const val channelName = "Experttalk"
    const val appId = "1cbef865a86c42b7851631f8e8a6e3ed"
    const val certificateId = "e2818d7d26a147ac8841eee0badfcdac"
    const val tempToken = "007eJxTYOh8np66XGIq/0GBbqfvwrU2r0/cua116bzTyud/WgM+6jxUYDBMTkpNszAzTbQwSzYxSjK3MDU0MzZMs0i1SDRLNU5NqY78mtwQyMjg80mAhZEBAkF8LgbXioLUopKSxJxsBgYA1Pwkaw=="
    const val signalingToken = "0061cbef865a86c42b7851631f8e8a6e3edIADk7jgpb7iMuWOcCbc00otIQSxEOnNZkbYaYJ8ezhzcxsOgvrkAAAAAEACej0AGMBbuYwEA6AMwFu5j"


    fun getSharedPref(context: Context) = context.getSharedPreferences("STATE",Context.MODE_PRIVATE)
    fun editor(context: Context) = getSharedPref(context).edit()

    fun getCurrentUserEmail(context: Context) = getSharedPref(context).getString("userEmail","").toString()

}
