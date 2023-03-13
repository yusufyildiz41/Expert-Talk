package com.yusufyildiz.experttalk.data.repository.video

import android.content.Context
import com.yusufyildiz.experttalk.common.Utils
import com.yusufyildiz.experttalk.domain.repository.video.VideoSdkEngineRepository
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration

class VideoSdkEngineRepositoryImpl() : VideoSdkEngineRepository {

    var agoraEnginee: RtcEngine? = null
    var videoConfig = VideoEncoderConfiguration()
    // Set mirror mode

    override suspend fun setupVideoSDKEngine(
        context: Context,
        config: RtcEngineConfig,
        mRtcEventHandler: IRtcEngineEventHandler
    ): RtcEngine {
        config.mContext = context
        config.mAppId = Utils.appId
        config.mEventHandler = mRtcEventHandler
        agoraEnginee = RtcEngine.create(config)
        agoraEnginee?.enableVideo()
        agoraEnginee?.enableDualStreamMode(true)
        agoraEnginee?.setAudioProfile(
            Constants.AUDIO_PROFILE_DEFAULT,
            Constants.AUDIO_SCENARIO_GAME_STREAMING
        )
        videoConfig.mirrorMode = VideoEncoderConfiguration.MIRROR_MODE_TYPE.MIRROR_MODE_AUTO
         // Set framerate
        videoConfig.frameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_60.value
         // Set bitrate
        videoConfig.bitrate = 3000
          // Set dimensions
        videoConfig.dimensions = VideoEncoderConfiguration.VD_1920x1080
         // Set orientation mode
        videoConfig.orientationMode =
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
         // Set degradation preference
        videoConfig.degradationPrefer =
            VideoEncoderConfiguration.DEGRADATION_PREFERENCE.MAINTAIN_BALANCED
         // Apply the configuration
        agoraEnginee?.setVideoEncoderConfiguration(videoConfig)

        return agoraEnginee!!
    }

    override suspend fun setupRemoteVideo(videoCanvas: VideoCanvas, agoraEngine: RtcEngine) {
        agoraEngine.setupRemoteVideo(
            videoCanvas
        )
    }

    override suspend fun setupLocalVideo(videoCanvas: VideoCanvas, agoraEngine: RtcEngine) {
        agoraEngine.setupLocalVideo(
            videoCanvas
        )
    }
}