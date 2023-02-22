package com.yusufyildiz.experttalk.ui.videocalling

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.data.video.VideoSdkEngineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCallingViewModel @Inject constructor(
    private val videoSdkEngineRepository: VideoSdkEngineRepository
) : ViewModel() {

    var agoraVideoViewer = MutableLiveData<RtcEngine>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("message : ${throwable.localizedMessage}")
    }

    fun setupVideoSdkEngine(
        context: Context,
        config: RtcEngineConfig,
        mIRtcEngineEventHandler: IRtcEngineEventHandler,
    ){
        viewModelScope.launch(exceptionHandler) {
           val agoraEngine =  videoSdkEngineRepository.setupVideoSDKEngine(
                context,config, mIRtcEngineEventHandler
            )
            agoraVideoViewer.value = agoraEngine
        }
    }

    fun setupLocalVideo(videoCanvas: VideoCanvas,agoraEngine: RtcEngine){
        viewModelScope.launch(exceptionHandler) {
            videoSdkEngineRepository.setupLocalVideo(videoCanvas,agoraEngine)
        }
    }

    fun setupRemoteVide(videoCanvas: VideoCanvas,agoraEngine: RtcEngine) {
        viewModelScope.launch(exceptionHandler) {
            videoSdkEngineRepository.setupRemoteVideo(videoCanvas,agoraEngine)
        }
    }

    fun destroyRtcEngine(){
        viewModelScope.launch {
            RtcEngine.destroy()
        }
    }

}