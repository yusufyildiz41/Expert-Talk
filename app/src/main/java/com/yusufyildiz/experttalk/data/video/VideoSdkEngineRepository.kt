package com.yusufyildiz.experttalk.data.video

import android.content.Context
import android.view.SurfaceView
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas

interface VideoSdkEngineRepository {
    suspend fun setupVideoSDKEngine(context: Context, config: RtcEngineConfig, mIRtcEngineEventHandler: IRtcEngineEventHandler): RtcEngine
    suspend fun setupLocalVideo(videoCanvas: VideoCanvas,agoraEngine: RtcEngine)
    suspend fun setupRemoteVideo(videoCanvas: VideoCanvas,agoraEngine: RtcEngine)
}