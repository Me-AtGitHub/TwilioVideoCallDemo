package com.example.videocalling

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.videocalling.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.twilio.video.CameraCapturer
import com.twilio.video.ConnectOptions
import com.twilio.video.LocalAudioTrack
import com.twilio.video.LocalVideoTrack
import com.twilio.video.RemoteAudioTrack
import com.twilio.video.RemoteAudioTrackPublication
import com.twilio.video.RemoteDataTrack
import com.twilio.video.RemoteDataTrackPublication
import com.twilio.video.RemoteParticipant
import com.twilio.video.RemoteVideoTrack
import com.twilio.video.RemoteVideoTrackPublication
import com.twilio.video.Room
import com.twilio.video.TwilioException
import com.twilio.video.Video
import com.twilio.video.VideoCapturer


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val option = ConnectOptions.Builder("TOKEN_ONE").build()
        Video.connect(this, option, getRoomListener())
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (key in it.keys) {
                Log.d(TAG, "permissionLauncher: key : $key ||  value : ${it[key]}")
            }
        }

    private fun checkPermission(context: Context) {

        /*Manifest.permission.READ_MEDIA_VIDEO
        Manifest.permission.READ_MEDIA_AUDIO
        Manifest.permission.READ_MEDIA_IMAGES
        Manifest.permission.CAMERA
        Manifest.permission.RECORD_AUDIO
        Manifest.permission.CAPTURE_AUDIO_OUTPUT*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        } else {

        }
    }


    private fun getRemoteParticipantsListener(): RemoteParticipant.Listener {
        return object : RemoteParticipant.Listener {
            override fun onAudioTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(TAG, "onAudioTrackPublished: ")
            }

            override fun onAudioTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(TAG, "onAudioTrackUnpublished: ")
            }

            override fun onAudioTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                Log.d(TAG, "onAudioTrackSubscribed: ")
            }

            override fun onAudioTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                twilioException: TwilioException
            ) {
                Log.d(TAG, "onAudioTrackSubscriptionFailed: ")
            }

            override fun onAudioTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                Log.d(TAG, "onAudioTrackUnsubscribed: ")
            }

            override fun onVideoTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(TAG, "onVideoTrackPublished: ")
            }

            override fun onVideoTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(TAG, "onVideoTrackUnpublished: ")
            }

            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                Log.d(TAG, "onVideoTrackSubscribed: ")
            }

            override fun onVideoTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                twilioException: TwilioException
            ) {
                Log.d(TAG, "onVideoTrackSubscriptionFailed: ")
            }

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                Log.d(TAG, "onVideoTrackUnsubscribed: ")
            }

            override fun onDataTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                Log.d(TAG, "onDataTrackPublished: ")
            }

            override fun onDataTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                Log.d(TAG, "onDataTrackUnpublished: ")
            }

            override fun onDataTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                Log.d(TAG, "onDataTrackSubscribed: ")
            }

            override fun onDataTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                twilioException: TwilioException
            ) {
                Log.d(TAG, "onDataTrackSubscriptionFailed: ")
            }

            override fun onDataTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                Log.d(TAG, "onDataTrackUnsubscribed: ")
            }

            override fun onAudioTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(TAG, "onAudioTrackEnabled: ")
            }

            override fun onAudioTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(TAG, "onAudioTrackDisabled: ")
            }

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(TAG, "onVideoTrackEnabled: ")
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(TAG, "onVideoTrackDisabled: ")
            }

        }
    }

    private fun getRoomListener(): Room.Listener {
        return object : Room.Listener {
            override fun onConnected(room: Room) {
                Log.d(TAG, "onConnected: ")
                room.localParticipant?.let { localParticipant ->
                    Log.d(TAG, "onConnected: localParticipant ")
                }
                room.remoteParticipants.let { remoteParticipants ->

                }

                // Share your microphone
                val localAudio =
                    LocalAudioTrack.create(this@MainActivity, true, "LOCAL_AUDIO_TRACK_NAME")
                // Share your Video
                val cameraId = System.currentTimeMillis().toString()
                val camera = CameraCapturer(this@MainActivity, cameraId)
                val localVideo = LocalVideoTrack.create(
                    this@MainActivity,
                    true,
                    camera,
                    "LOCAL_AUDIO_TRACK_NAME"
                )

                binding.videoView.mirror = true
                localVideo
            }

            override fun onConnectFailure(room: Room, twilioException: TwilioException) {
                Log.d(TAG, "onConnectFailure:${twilioException.code} ")
            }

            override fun onReconnecting(room: Room, twilioException: TwilioException) {
                Log.d(TAG, "onReconnecting: ")
            }

            override fun onReconnected(room: Room) {
                Log.d(TAG, "onReconnected: ")
            }

            override fun onDisconnected(room: Room, twilioException: TwilioException?) {
                Log.d(TAG, "onDisconnected: ")
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                Log.d(TAG, "onParticipantConnected: ")
            }

            override fun onParticipantDisconnected(
                room: Room, remoteParticipant: RemoteParticipant
            ) {
                Log.d(TAG, "onParticipantDisconnected: ")
            }

            override fun onRecordingStarted(room: Room) {
                Log.d(TAG, "onRecordingStarted: ")
            }

            override fun onRecordingStopped(room: Room) {
                Log.d(TAG, "onRecordingStopped: ")
            }

        }
    }

    companion object {


        const val TAG = "MainActivityTesting"
        const val TWILIO_ACCOUNT_SID = "AC31ceb502473f217202e38af087e12918"
        const val TWILIO_API_KEY = "SKbf8d290cb0ed43f1a345a7c305393467"
        const val TWILIO_API_SECRET = "Tkg6oYkM9PphhNrkoxzLkNnCszEQqnYR"

//        const val TWILIO_API_SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    }

}
