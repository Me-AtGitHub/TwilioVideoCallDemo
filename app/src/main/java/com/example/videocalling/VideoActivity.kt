package com.example.videocalling

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.videocalling.databinding.ActivityMainBinding
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
import com.twilio.video.VideoTrack
import tvi.webrtc.Camera1Enumerator
import tvi.webrtc.RendererCommon
import java.util.Collections

class VideoActivity : AppCompatActivity() {

    companion object {
        const val TOKEN_ONE =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzdkOTJiYTUyMTdlZDM0MzkzOTQxOGIwZmI1Y2I4MmFiLTE2ODczMjQ3NTgiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ2aWthc09uZSIsInZpZGVvIjp7InJvb20iOiJNeVRlc3RSb29tIn19LCJpYXQiOjE2ODczMjQ3NTgsImV4cCI6MTY4NzMyODM1OCwiaXNzIjoiU0s3ZDkyYmE1MjE3ZWQzNDM5Mzk0MThiMGZiNWNiODJhYiIsInN1YiI6IkFDMTk4NjYyMjc1MmI5YzgwNGYzZjRhYmM4OTllNjYzZDgifQ.Kp3iKUsCE8quaFSt_uXGTNPYyBhwZy_r6zESFPXMfAo"
        const val TOKEN_TWO =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzdkOTJiYTUyMTdlZDM0MzkzOTQxOGIwZmI1Y2I4MmFiLTE2ODczMjQ3OTUiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ2aWthc1R3byIsInZpZGVvIjp7InJvb20iOiJNeVRlc3RSb29tIn19LCJpYXQiOjE2ODczMjQ3OTUsImV4cCI6MTY4NzMyODM5NSwiaXNzIjoiU0s3ZDkyYmE1MjE3ZWQzNDM5Mzk0MThiMGZiNWNiODJhYiIsInN1YiI6IkFDMTk4NjYyMjc1MmI5YzgwNGYzZjRhYmM4OTllNjYzZDgifQ.juaiHwjEZtpeoZDZQF99lk3c0bZQ9oyJaD2ULaseaWc"
    }

    private var accessToken = ""
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        accessToken = TOKEN_TWO
        connectToRoom("MyTestRoom")
    }


    /**
     * Connect to a room with accessToken and RoomName...
     *
     * Sets a [Room.Listener] Room connection listener to the room
     * */
    private fun connectToRoom(roomName: String) {

        val connectOptionsBuilder = ConnectOptions.Builder(accessToken).roomName(roomName)
        val localAudioTrack = LocalAudioTrack.create(this, true)
        connectOptionsBuilder.audioTracks(Collections.singletonList(localAudioTrack))

        for (name in Camera1Enumerator().deviceNames) {
            Log.d(MainActivity.TAG, "DeviceNames: $name")
        }

        val localVideoTrack = LocalVideoTrack.create(
            this,
            true,
            CameraCapturer(
                this,
                Camera1Enumerator().deviceNames[0],
                object : CameraCapturer.Listener {
                    override fun onFirstFrameAvailable() {
                        Log.d(MainActivity.TAG, "onFirstFrameAvailable: ")
                    }

                    override fun onCameraSwitched(newCameraId: String) {
                        Log.d(MainActivity.TAG, "onCameraSwitched: $newCameraId")
                    }

                    override fun onError(errorCode: Int) {
                        Log.d(MainActivity.TAG, "onError: $errorCode")
                    }

                })
        )

        connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack))
        localVideoTrack?.addSink(binding.secondaryVideoView)
        Video.connect(this, connectOptionsBuilder.build(), roomListener())
        binding.videoView.mirror = false
        binding.videoView.setListener(object : RendererCommon.RendererEvents {
            override fun onFirstFrameRendered() {
                Log.d(MainActivity.TAG, "RendererCommon onFirstFrameRendered: ")
            }

            override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) {
                Log.d(MainActivity.TAG, "onFrameResolutionChanged: ")
            }

        })

    }

    /**
     * You will listen room connection events here
     * Like [Room.Listener.onConnected]
     * */
    private fun roomListener(): Room.Listener {
        return object : Room.Listener {

            override fun onConnected(room: Room) {
                Log.d(MainActivity.TAG, "onConnected: ")
                // If there are remote participants already in this particular room then show there video as well
                room.remoteParticipants.forEach {
                    Log.d(MainActivity.TAG, "onConnectedWith: ${it.identity}")
                    it.setListener(remoteParticipantListener())
                }
            }

            override fun onConnectFailure(room: Room, twilioException: TwilioException) {
                Log.d(MainActivity.TAG, "onConnectFailure:${twilioException.code} ")
            }

            override fun onReconnecting(room: Room, twilioException: TwilioException) {
                Log.d(MainActivity.TAG, "onReconnecting: ")
            }

            override fun onReconnected(room: Room) {
                Log.d(MainActivity.TAG, "onReconnected: ")
            }

            override fun onDisconnected(room: Room, twilioException: TwilioException?) {
                Log.d(MainActivity.TAG, "onDisconnected: ")
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                Log.d(MainActivity.TAG, "onParticipantConnected: ")
                remoteParticipant.setListener(remoteParticipantListener())
            }

            override fun onParticipantDisconnected(
                room: Room, remoteParticipant: RemoteParticipant
            ) {
                remoteParticipant.setListener(remoteParticipantListener())
                Log.d(MainActivity.TAG, "onParticipantDisconnected: ")
            }

            override fun onRecordingStarted(room: Room) {
                Log.d(MainActivity.TAG, "onRecordingStarted: ")
            }

            override fun onRecordingStopped(room: Room) {
                Log.d(MainActivity.TAG, "onRecordingStopped: ")
            }

        }
    }

    private fun remoteParticipantListener(): RemoteParticipant.Listener {
        return object : RemoteParticipant.Listener {

            override fun onAudioTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onAudioTrackPublished: ")

                Log.d(
                    MainActivity.TAG, String.format(
                        "onAudioTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.identity,
                        remoteAudioTrackPublication.trackSid,
                        remoteAudioTrackPublication.isTrackEnabled,
                        remoteAudioTrackPublication.isTrackSubscribed,
                        remoteAudioTrackPublication.trackName
                    )
                );

            }

            override fun onAudioTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onAudioTrackUnpublished: ")
            }

            override fun onAudioTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                remoteAudioTrack.addSink { audioSample, encoding, sampleRate, channels ->
                    audioSample
                }
                Log.d(MainActivity.TAG, "onAudioTrackSubscribed: ")

            }

            override fun onAudioTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                twilioException: TwilioException
            ) {
                Log.d(MainActivity.TAG, "onAudioTrackSubscriptionFailed: ")
            }

            override fun onAudioTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                Log.d(MainActivity.TAG, "onAudioTrackUnsubscribed: ")
            }

            override fun onVideoTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackPublished: ")
            }

            override fun onVideoTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackUnpublished: ")
            }

            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackSubscribed: ")
                Log.d(
                    MainActivity.TAG, String.format(
                        "onVideoTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()
                    )
                )
                addRemoteParticipantVideo(remoteVideoTrack)
            }

            override fun onVideoTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                twilioException: TwilioException
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackSubscriptionFailed: ")
            }

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackUnsubscribed: ")
                Log.d(
                    MainActivity.TAG, String.format(
                        "onVideoTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.identity,
                        remoteVideoTrack.isEnabled,
                        remoteVideoTrack.name
                    )
                )
                removeParticipantVideo(remoteVideoTrack);
            }

            override fun onDataTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onDataTrackPublished: ")
            }

            override fun onDataTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onDataTrackUnpublished: ")
            }

            override fun onDataTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                Log.d(MainActivity.TAG, "onDataTrackSubscribed: ")
            }

            override fun onDataTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                twilioException: TwilioException
            ) {
                Log.d(MainActivity.TAG, "onDataTrackSubscriptionFailed: ")
            }

            override fun onDataTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                Log.d(MainActivity.TAG, "onDataTrackUnsubscribed: ")
            }

            override fun onAudioTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onAudioTrackEnabled: ")
            }

            override fun onAudioTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onAudioTrackDisabled: ")
            }

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackEnabled: ")
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.d(MainActivity.TAG, "onVideoTrackDisabled: ")
            }

        }
    }

    private fun removeParticipantVideo(videoTrack: VideoTrack) {
        videoTrack.removeSink(binding.videoView)
    }

    /**
     * Set video view as sink for participant video track
     */
    private fun addRemoteParticipantVideo(videoTrack: VideoTrack) {
        Log.d(MainActivity.TAG, "Added remote participant")
        Log.d(MainActivity.TAG, videoTrack.name)
        videoTrack.addSink(binding.videoView)
    }

    private fun addRemoteParticipantsAudio(audioTrack: RemoteAudioTrack) {
        Log.d(MainActivity.TAG, "addRemoteParticipantsAudio: ")
//        audioTrack.addSink(binding.videoView)
    }

}

