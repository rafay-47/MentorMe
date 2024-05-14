package com.AbdulRafay.i212582

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

class VoiceCall : AppCompatActivity() {
    private var mRtcEngine: RtcEngine? = null
    private val TAG = "VoiceCallActivity"

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            Log.i(TAG, "Successfully joined channel: $channel")
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.i(TAG, "Remote user joined: $uid")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            Log.i(TAG, "Remote user left: $uid")
        }

        override fun onLeaveChannel(stats: RtcStats?) {
            Log.i(TAG, "Left channel")
        }

        override fun onError(err: Int) {
            Log.e(TAG, "Error: $err")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_call)

        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQ_ID)
        } else {
            // Initialize and join the channel if permissions are already granted
            initializeAndJoinChannel()
        }

        findViewById<ImageView>(R.id.img1).setOnClickListener {
            leaveChannel()
            finish()
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, "edfebe07dcc44d1fb65b00bdff7af17f", mRtcEventHandler)
            mRtcEngine?.joinChannel(null, "yourChannelName", "Extra Optional Data", 0)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Agora RtcEngine: ${e.localizedMessage}")
        }
    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine?.let {
            it.leaveChannel()
            RtcEngine.destroy()
            mRtcEngine = null
        }
    }


    companion object {
        const val PERMISSION_REQ_ID = 22
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeAndJoinChannel()
            } else {
                Log.i(TAG, "Audio permission not granted")
            }
        }
    }
}
