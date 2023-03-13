package com.yusufyildiz.experttalk.ui.videocalling

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.Utils
import com.yusufyildiz.experttalk.databinding.FragmentVideoCallingBinding
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.*


@AndroidEntryPoint
class VideoCallingFragment : Fragment() {

    private var uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private var control = true
    private var mMuted = false

    private var counter1 = 0
    private var counter2 = 0
    private var remoteUid : Int?=null
    private var highQuality = true

    private var localSurfaceView: SurfaceView? = null
    private var remoteSurfaceView: SurfaceView? = null

    private var remoteJob: Job? = null
    private var localJob: Job? = null

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var binding: FragmentVideoCallingBinding
    private val videoCallingViewModel: VideoCallingViewModel by viewModels()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error message -> ${throwable.localizedMessage}")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoCallingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLauncher()
        val permissionLauncherShouldRequet =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
            }

        setupVideoSDKEngine()

        with(binding) {

            buttonCall.setImageResource(R.drawable.btn_startcall)
            buttonSwitchCamera.visibility = View.INVISIBLE
            buttonMute.visibility = View.INVISIBLE

            buttonCall.setOnClickListener {
                if (
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Utils.permissions[0]
                    ) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Utils.permissions[1]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    shouldShowRationale(permissionLauncherShouldRequet)
                } else {
                    //permission granted
                    showMessage(requireContext(), "Permission Granted")
                    Log.e("success", "successfully")

                    if (control) {
                        startCall()
                        control = false
                        buttonCall.setImageResource(R.drawable.btn_endcall)
                        buttonMute.visibility = View.VISIBLE
                        buttonSwitchCamera.visibility = View.VISIBLE
                        buttonCall.setOnClickListener {
                            findNavController().navigate(R.id.action_videoCallingFragment_to_signInFragment)
                        }
                    } else {
                        endCall()
                        control = true
                        buttonCall.setImageResource(R.drawable.btn_startcall)
                        buttonMute.visibility = View.INVISIBLE
                        buttonSwitchCamera.visibility = View.INVISIBLE

                    }
                }
            }

            buttonSwitchCamera.setOnClickListener {
                videoCallingViewModel.agoraVideoViewer.observe(viewLifecycleOwner) { rtcEngine ->
                    rtcEngine?.switchCamera()
                }
            }

            buttonMute.setOnClickListener {
                mMuted = !mMuted
                videoCallingViewModel.agoraVideoViewer.observe(viewLifecycleOwner) { rtcEngine ->
                    rtcEngine?.muteLocalAudioStream(mMuted)
                }
                val res: Int = if (mMuted) {
                    R.drawable.btn_mute
                } else {
                    R.drawable.btn_unmute
                }

                buttonMute.setImageResource(res)
            }
        }
    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
                showMessage(requireContext(), "on user joined")
                setUpRemoteVideo(uid)
            }
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
                isJoined = true
                showMessage(requireContext(), "User joined")
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            CoroutineScope(Dispatchers.Main + exceptionHandler).launch {
                onRemoteUserLeft()
            }
        }

        override fun onConnectionStateChanged(state: Int, reason: Int) {
            super.onConnectionStateChanged(state, reason)
            CoroutineScope(Dispatchers.Main+ exceptionHandler).launch{
                showMessage(requireContext(),"Bağlantı durumuz değişti \n Yeni durum: ${state} \n Sebep: ${reason} ")
            }
        }

        override fun onLastmileQuality(quality: Int) {
            super.onLastmileQuality(quality)
            CoroutineScope(Dispatchers.Main+ exceptionHandler).launch{
                updateNetworkStatus(quality)
            }
        }

        override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
            super.onNetworkQuality(uid, txQuality, rxQuality)
            CoroutineScope(Dispatchers.Main+exceptionHandler).launch {
                updateNetworkStatus(rxQuality)
            }
        }

        override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed)

            var message = "Uzaktan video durumu değişti: \n Uid = ${uid} \n Yeni durum: ${state} \n Sebep: ${reason} \n Geçen: ${elapsed} "
            CoroutineScope(Dispatchers.Main+exceptionHandler).launch {
                showMessage(requireContext(),message)
            }
        }
    }


    private fun setupVideoSDKEngine() {
        val config = RtcEngineConfig()
        videoCallingViewModel.setupVideoSdkEngine(requireContext(), config, mRtcEventHandler)
    }

    private fun setUpRemoteVideo(uid: Int) {
        remoteJob = lifecycleScope.launch {
            remoteSurfaceView = SurfaceView(context)
            binding.remoteVideoView.addView(remoteSurfaceView)
            val videoCanvas = VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                uid
            )
            videoCallingViewModel.agoraVideoViewer.observe(viewLifecycleOwner) { rtcEngine ->
                rtcEngine?.let {
                    videoCallingViewModel.setupRemoteVide(videoCanvas, it)
                }
            }
        }
    }

    private fun setUpLocalVideo() {

        val options = ChannelMediaOptions()
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER

        localJob = lifecycleScope.launch {
            localSurfaceView = SurfaceView(context)
            localSurfaceView!!.setZOrderMediaOverlay(true)
            binding.localVideoView.addView(localSurfaceView)
            val videoCanvas = VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )
            videoCallingViewModel.agoraVideoViewer.observe(viewLifecycleOwner) { rtcEngine ->
                rtcEngine?.let {
                    videoCallingViewModel.setupLocalVideo(videoCanvas, it).apply {
                        it.startPreview()
                        it.joinChannel(Utils.tempToken, Utils.channelName, uid, options)
                    }
                }
            }
        }
    }

    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perm ->
            if (perm[Utils.permissions[0]] == true && perm[Utils.permissions[1]] == true) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Utils.permissions[0]
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Utils.permissions[1]
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //permission granted
                    showMessage(requireContext(), "Permission Granted")
                }
            } else {
                showMessage(requireContext(), "Permission Needed!!")
            }
        }
    }

    private fun shouldShowRationale(permissionLauncher: ActivityResultLauncher<Array<String>>) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Utils.permissions[0]
            )
            && ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Utils.permissions[1]
            )
        ) {
            Snackbar.make(
                binding.root,
                "Permission needed for video calling",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Give Permission") {
                permissionLauncher.launch(
                    Utils.permissions
                )
            }.show()
        } else {
            permissionLauncher.launch(
                Utils.permissions
            )
        }
    }


    private fun startCall() {
        setUpLocalVideo()
    }

    private fun endCall() {
        removeLocalVideo()
        removeRemoteVideo()
        leaveChannel()
    }

    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun removeRemoteVideo() {
        if (remoteSurfaceView != null) {
            binding.remoteVideoView.removeView(remoteSurfaceView)
        }
        remoteSurfaceView = null
    }

    private fun removeLocalVideo() {
        if (localSurfaceView != null) {
            binding.localVideoView.removeView(localSurfaceView)
        }
        localSurfaceView = null
    }

    private fun onRemoteUserLeft() {
        removeRemoteVideo()
    }

    private fun leaveChannel() {
        agoraEngine?.leaveChannel()
    }

    private fun updateNetworkStatus(quality: Int){
        with(binding){
            if(quality > 0 && quality < 3){
                networkStatus.setBackgroundColor(Color.GREEN)
            }
            else if (quality <= 4){
                networkStatus.setBackgroundColor(Color.YELLOW)
            }
            else if (quality <= 6){
                networkStatus.setBackgroundColor(Color.RED)
            }
            else {
                networkStatus.setBackgroundColor(Color.WHITE)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (!control) {
            agoraEngine?.stopPreview()
            agoraEngine?.leaveChannel()
            remoteJob?.cancel()
            localJob?.cancel()
            leaveChannel()
        }
        RtcEngine.destroy()
        videoCallingViewModel.destroyRtcEngine()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!control) {
            agoraEngine?.stopPreview()
            agoraEngine?.leaveChannel()
            remoteJob?.cancel()
            localJob?.cancel()
            leaveChannel()
        }
        RtcEngine.destroy()
        videoCallingViewModel.destroyRtcEngine()
    }
}