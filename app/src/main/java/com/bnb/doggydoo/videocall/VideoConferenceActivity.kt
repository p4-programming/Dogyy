package com.bnb.doggydoo.videocall

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.google.gson.Gson
import enx_rtc_android.Controller.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class VideoConferenceActivity : AppCompatActivity(), EnxRoomObserver, EnxStreamObserver,
    EnxLogsObserver, View.OnClickListener, EnxReconnectObserver, EnxActiveTalkerViewObserver {

    private var enxRtc: EnxRtc? = null
    private var token: String? = ""
    private var name: String? = ""
    private var requestType: String = ""
    private var enxPlayerView: EnxPlayerView? = null
    private var moderator: FrameLayout? = null
    private var participant: FrameLayout? = null
    private var disconnect: ImageView? = null
    private var mute: ImageView? = null
    private var video: ImageView? = null
    private var camera: ImageView? = null
    private var volume: ImageView? = null
    private var enxRooms: EnxRoom? = null
    private var isVideoMuted = false
    private var isFrontCamera = true
    private var isAudioMuted = false
    private var enxLogsUtil: EnxUtilityManager? = null
    private var rl: RelativeLayout? = null
    private var gson: Gson? = null
    private var localStream: EnxStream? = null
    private var enxPlayerViewRemote: EnxPlayerView? = null
    private var PERMISSION_ALL = 1
    private var PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_APN_SETTINGS
    )
    var mRecyclerView: RecyclerView? = null

    private val localStreamJsonObject: JSONObject
        get() {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("audio", true)
                jsonObject.put("video", true)
                jsonObject.put("data", false)
                val videoSize = JSONObject()
                videoSize.put("minWidth", 320)
                videoSize.put("minHeight", 180)
                videoSize.put("maxWidth", 1280)
                videoSize.put("maxHeight", 720)
                jsonObject.put("videoSize", videoSize)
                jsonObject.put("audioMuted", false)
                jsonObject.put("videoMuted", false)
                jsonObject.put("name", name)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return jsonObject
        }

    private val localStreamJsonObject1: JSONObject
        get() {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("audio", true)
                jsonObject.put("video", false)
                jsonObject.put("data", false)
                val videoSize = JSONObject()
                videoSize.put("minWidth", 320)
                videoSize.put("minHeight", 180)
                videoSize.put("maxWidth", 1280)
                videoSize.put("maxHeight", 720)
                jsonObject.put("videoSize", videoSize)
                jsonObject.put("audioMuted", false)
                jsonObject.put("videoMuted", false)
                jsonObject.put("name", name)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return jsonObject
        }


    private val roomInfo: JSONObject
        get() {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("allow_reconnect", true)
                jsonObject.put("number_of_attempts", 3)
                jsonObject.put("timeout_interval", 10000)
                jsonObject.put("activeviews", "view") //view

                val jobject = JSONObject()
                jobject.put("audiomute", true)
                jobject.put("videomute", true)
                jobject.put("bandwidth", true)
                jobject.put("screenshot", true)
                jobject.put("avatar", true)
                jobject.put("iconColor", ContextCompat.getColor(this, R.color.black))
                jobject.put("iconHeight", 30)
                jobject.put("iconWidth", 30)
                jobject.put("avatarHeight", 200)
                jobject.put("avatarWidth", 200)


                jsonObject.put("playerConfiguration", jobject)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return jsonObject
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_conference)
        getPreviousIntent()

        if (!hasPermissions(this, *PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL)
        } else {
            initialize()
        }
    }

    private fun initialize() {
        setUI()
        setClickListener()
        gson = Gson()
        enxRtc = EnxRtc(this, this, this)
        enxLogsUtil = EnxUtilityManager.getInstance()
        enxLogsUtil!!.enableLogs(true)

        if (requestType == "1") {
            localStream = enxRtc!!.joinRoom(token, localStreamJsonObject, roomInfo, JSONArray())
            moderator!!.show()
        } else {
            localStream = enxRtc!!.joinRoom(token, localStreamJsonObject1, roomInfo, JSONArray())
            moderator!!.hide()
        }

        enxPlayerView = EnxPlayerView(this, EnxPlayerView.ScalingType.SCALE_ASPECT_BALANCED, true)
        Log.e("localStream", localStream!!.toString())
        localStream!!.attachRenderer(enxPlayerView)
        moderator?.addView(enxPlayerView)
    }

    private fun setClickListener() {
        disconnect?.setOnClickListener(this)
        mute?.setOnClickListener(this)
        video?.setOnClickListener(this)
        camera?.setOnClickListener(this)
        volume?.setOnClickListener(this)
        moderator?.setOnTouchListener(OnDragTouchListener(moderator!!))
    }

    private fun setUI() {
        moderator = findViewById<View>(R.id.moderator) as FrameLayout
        participant = findViewById<View>(R.id.participant) as FrameLayout
        disconnect = findViewById<View>(R.id.disconnect) as ImageView
        mute = findViewById<View>(R.id.mute) as ImageView
        video = findViewById<View>(R.id.video) as ImageView
        camera = findViewById<View>(R.id.camera) as ImageView
        volume = findViewById<View>(R.id.volume) as ImageView
        rl = findViewById<View>(R.id.rl) as RelativeLayout
    }

    private fun getPreviousIntent() {
        if (intent != null) {
            token = intent.getStringExtra("token").toString()
            name = intent.getStringExtra("name").toString()
            requestType = intent.getStringExtra("request_type").toString()
        }

    }

    override fun onRoomConnected(enxRoom: EnxRoom?, jsonObject: JSONObject) {
        //received when user connected with Enablex room
        enxRooms = enxRoom
        enxRooms?.publish(localStream)
        enxRooms?.setReconnectObserver(this)
        enxRooms?.setActiveTalkerViewObserver(this)

        /*if (requestType == "1") {
            enxRooms?.setAudioOnlyMode(false)
            moderator!!.show()
        } else {
            enxRooms?.setAudioOnlyMode(true)
            moderator!!.hide()
        }*/
    }

    override fun onRoomError(jsonObject: JSONObject) {
        //received when any error occurred while connecting to the Enablex room
        Toast.makeText(
            this@VideoConferenceActivity,
            jsonObject.optString("msg"),
            Toast.LENGTH_SHORT
        ).show()
        finish()


    }

    override fun onUserConnected(jsonObject: JSONObject) {
        // received when a new remote participant joins the call

    }

    override fun onUserDisConnected(jsonObject: JSONObject) {
        // received when a  remote participant left the call
        roomDisconnect()
    }

    private fun roomDisconnect() {
        if (enxRooms != null) {
            if (enxPlayerView != null) {
                enxPlayerView!!.release()
                enxPlayerView = null
            }
            if (enxPlayerViewRemote != null) {
                enxPlayerViewRemote!!.release()
                enxPlayerViewRemote = null
            }
            enxRooms!!.disconnect()
        } else {
            this.finish()
        }
    }

    override fun onMessageReceived(p0: JSONObject?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUserDataReceived(p0: JSONObject?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEventInfo(p0: JSONObject?) {
        // received for different events update
    }

    override fun onSwitchedUserRole(p0: JSONObject?) {
        // received when user switch their role (from moderator  to participant)
    }

    override fun onAcknowledgedSendData(p0: JSONObject?) {
        // received your chat data successfully sent to the other end
    }

    override fun onConferencessExtended(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onUserRoleChanged(p0: JSONObject?) {
        // received when user role changed successfully
    }

    override fun onReconnect(p0: String?) {
        // received when room tries to reconnect due to low bandwidth or any connection interruption
        Toast.makeText(this, p0, Toast.LENGTH_SHORT).show()
        initialize()
    }

    override fun onUserReconnectSuccess(p0: EnxRoom?, p1: JSONObject?) {
        // received when reconnect successfully completed
    }

    override fun onNotifyDeviceUpdate(p0: String?) {
        // received when when new media device changed
    }

    override fun onPublishedStream(enxStream: EnxStream) {
        //received when audio video published successfully to the other remote users
    }

    override fun onUnPublishedStream(enxStream: EnxStream) {
        //received when audio video unpublished successfully to the other remote users
    }

    override fun onLogUploaded(p0: JSONObject?) {
        //received when logs successfully uploaded
    }

    override fun onConferenceRemainingDuration(p0: JSONObject?) {
        TODO("Not yet implemented")
    }


    override fun onStreamAdded(enxStream: EnxStream?) {
        //received when a new stream added
        if (enxStream != null) {
            enxRooms!!.subscribe(enxStream)
        }
    }

    override fun onSubscribedStream(enxStream: EnxStream) {
        //received when a remote stream subscribed successfully
    }

    override fun onUnSubscribedStream(enxStream: EnxStream) {
        //received when a remote stream unsubscribed successfully
    }

    override fun onRoomDisConnected(jsonObject: JSONObject) {
        //received when Enablex room successfully disconnected
        this.finish()
    }

    override fun onActiveTalkerList(p0: androidx.recyclerview.widget.RecyclerView?) {
        mRecyclerView = p0
        if (p0 == null) {
            participant!!.removeAllViews()
        } else {
            participant!!.removeAllViews()
            participant!!.addView(p0)
        }
    }

    override fun onEventError(jsonObject: JSONObject) {
        //received when any error occurred for any room event
        runOnUiThread {
            Toast.makeText(
                this@VideoConferenceActivity,
                jsonObject.optString("msg"),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onAckDestroy(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onAckDropUser(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onAudioEvent(jsonObject: JSONObject) {
        //received when audio mute/unmute happens
        Toast.makeText(this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show()
        try {
            val message = jsonObject.getString("msg")
            if (message.equals("Audio On", ignoreCase = true)) {
                mute?.setImageResource(R.drawable.mute)
                isAudioMuted = false
            } else if (message.equals("Audio Off", ignoreCase = true)) {
                mute?.setImageResource(R.drawable.unmute)
                isAudioMuted = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onVideoEvent(jsonObject: JSONObject) {
        //received when video mute/unmute happens
        Toast.makeText(this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show()
        try {
            val message = jsonObject.getString("msg")
            if (message.equals("Video On", ignoreCase = true)) {
                video?.setImageResource(R.drawable.ic_videocam)
                isVideoMuted = false
            } else if (message.equals("Video Off", ignoreCase = true)) {
                video?.setImageResource(R.drawable.ic_videocam_off)
                isVideoMuted = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onReceivedData(jsonObject: JSONObject) {
        //received when chat data received at room level
    }

    override fun onRemoteStreamAudioMute(jsonObject: JSONObject) {
        //received when any remote stream mute audio
    }

    override fun onRemoteStreamAudioUnMute(jsonObject: JSONObject) {
        //received when any remote stream unmute audio
    }

    override fun onRemoteStreamVideoMute(jsonObject: JSONObject) {
        //received when any remote stream mute video
    }

    override fun onRemoteStreamVideoUnMute(jsonObject: JSONObject) {
        //received when any remote stream unmute video
    }

    override fun onAckPinUsers(p0: JSONObject?) {

    }

    override fun onAckUnpinUsers(p0: JSONObject?) {

    }

    override fun onPinnedUsers(p0: JSONObject?) {

    }

    override fun onRoomAwaited(p0: EnxRoom?, p1: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onUserAwaited(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onAckForApproveAwaitedUser(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onAckForDenyAwaitedUser(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onAckAddSpotlightUsers(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onAckRemoveSpotlightUsers(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onUpdateSpotlightUsers(p0: JSONObject?) {
        TODO("Not yet implemented")
    }

    override fun onRoomBandwidthAlert(p0: JSONObject?) {
        val band = p0!!.optString("stream_bandwidth").toString()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.disconnect -> roomDisconnect()
            R.id.mute -> if (localStream != null) {
                if (!isAudioMuted) {
                    localStream!!.muteSelfAudio(true)
                } else {
                    localStream!!.muteSelfAudio(false)
                }
            }
            R.id.video -> if (localStream != null) {
                if (!isVideoMuted) {
                    localStream!!.muteSelfVideo(true)
                } else {
                    localStream!!.muteSelfVideo(false)
                }
            }
            R.id.camera -> if (localStream != null) {
                if (!isVideoMuted) {
                    if (isFrontCamera) {
                        localStream!!.switchCamera()
                        camera?.setImageResource(R.drawable.rear_camera)
                        isFrontCamera = false
                    } else {
                        localStream!!.switchCamera()
                        camera?.setImageResource(R.drawable.front_camera)
                        isFrontCamera = true
                    }
                }
            }
            R.id.volume -> if (enxRooms != null) {
                showRadioButtonDialog()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialize()
            } else {
                Toast.makeText(
                    this,
                    "Please enable permissions to further proceed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun showRadioButtonDialog() {
        val dialog = Dialog(this@VideoConferenceActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.radiogroup)
        val stringList = ArrayList<String>()  // here is list

        val deviceList = enxRooms!!.devices
        for (i in deviceList.indices) {
            stringList.add(deviceList[i])
        }
        val rg = dialog.findViewById<View>(R.id.radio_group) as RadioGroup
        val selectedDevice = enxRooms!!.selectedDevice
        if (selectedDevice != null) {
            for (i in stringList.indices) {
                val rb =
                    RadioButton(this@VideoConferenceActivity) // dynamically creating RadioButton and adding to RadioGroup.
                rb.text = stringList[i]
                rg.addView(rb)
                if (selectedDevice.equals(stringList[i], ignoreCase = true)) {
                    rb.isChecked = true
                }

            }
            dialog.show()
        }

        rg.setOnCheckedChangeListener { group, checkedId ->
            val childCount = group.childCount
            for (x in 0 until childCount) {
                val btn = group.getChildAt(x) as RadioButton
                if (btn.id == checkedId) {
                    Log.e("selected RadioButton->", btn.text.toString())
                    enxRooms!!.switchMediaDevice(btn.text.toString())
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onBackPressed() {
        //            super.onBackPressed();
    }

    override fun onPause() {
        super.onPause()
        if (enxRooms != null) {
            enxRooms!!.stopVideoTracksOnApplicationBackground(true, true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (enxRooms != null) {
            enxRooms!!.startVideoTracksOnApplicationForeground(true, true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (enxPlayerViewRemote != null) {
            enxPlayerViewRemote!!.release()
        }
        if (enxPlayerView != null) {
            enxPlayerView!!.release()
        }
        if (localStream != null) {
            localStream!!.detachRenderer()
        }
        if (enxRooms != null) {
            enxRooms = null
        }
        if (enxRtc != null) {
            enxRtc = null
        }
    }

}
