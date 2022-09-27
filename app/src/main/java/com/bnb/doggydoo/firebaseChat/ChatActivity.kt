package com.bnb.doggydoo.firebaseChat

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bnb.chat.FirebaseService
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityChatBinding
import com.bnb.doggydoo.firebaseChat.Notification.*
import com.bnb.doggydoo.firebaseChat.adapter.MessageAdapter
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant
import com.bnb.doggydoo.videocall.VideoConferenceActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private var userName: String = ""
    private var receiverUid: String = ""
    var senderRoom: String? = null
    var receiverRoom: String? = null
    private lateinit var mDbRef: DatabaseReference
    private var uri: Uri? = null
    private var senderUid: String? = null
    private lateinit var mAuth: FirebaseAuth
    private var _binding: ActivityChatBinding? = null
    private val binding get() = _binding!!
    private var userImage: String = ""
    private lateinit var firestore: FirebaseFirestore
    var apiserviceshit: APISERVICESHIT? = null
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    private val requestcode = 1
    private var firebaseToken: String = ""
    var clickedUserId: String = ""
    var clickedUserId1: String = ""
    private var fromValue:String =""
    private  var  reqType:String =""

    private val homeViewModel: HomeViewModel by viewModels()
    private var roomId: String = ""
    private var refNo: String = ""
    private var tokenIs: String = ""
    private var type: String = ""



    private val requestArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        ActivityCompat.requestPermissions(
            this,
            requestArray,
            1
        )

        mAuth = Firebase.auth
        mDbRef = FirebaseDatabase.getInstance().reference
        firestore = FirebaseFirestore.getInstance()
        apiserviceshit =
            Client.getRetrofit("https://fcm.googleapis.com/").create(APISERVICESHIT::class.java)

        userName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uid").toString()
        userImage = intent.getStringExtra("userImage").toString()
        firebaseToken = intent.getStringExtra("firebasetoken").toString()
        clickedUserId = intent.getStringExtra("clicked_user_id").toString()
        clickedUserId1 = intent.getStringExtra("viewuserid").toString()

        Log.d("Deepak", "ClickedUserid : $clickedUserId1")

        if (intent.getStringExtra("from").toString().trim() == "1"){
            fromValue =  "direct"
            reqType = intent.getStringExtra("reqType").toString()
        }else{
            fromValue =  "indirect"
        }


        senderUid = Firebase.auth.currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        messageList = ArrayList()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.txtUserName.text = userName

        if (userImage == "user.png") {
            binding.profileImage.setImageResource(R.drawable.dummy_user)
        } else {
            binding.profileImage.loadImageFromString(
                this,
                ApiConstant.PROFILE_IMAGE_BASE_URL + userImage
            )
        }

        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java)
                .putExtra("viewuserid",clickedUserId1)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            )
        }

        if (messageList.size > 0) {
            binding.llProgress.visibility = View.VISIBLE
            binding.tvAlert.text = "Loading..."
        } else {
            binding.llProgress.visibility = View.GONE
        }
        adapter = MessageAdapter(this@ChatActivity, userName, messageList)
        binding.rvChat.adapter = adapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    binding.llProgress.visibility = View.VISIBLE
                    binding.tvAlert.text = "Loading..."

                    for (postSnapShot in snapshot.children) {
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    binding.llProgress.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                    binding.rvChat.scrollToPosition(messageList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding.ivSendImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(500)            //Final image size will be less than 1 MB(Optional) //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        if (fromValue.isNotEmpty()){
            if (fromValue == "direct"){
                val message = "Hello, I am available and I have accepted your $reqType Request"
                val messageObject = Message(message, senderUid, "TEXT", "")
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                binding.etChat.setText("")
                mDbRef.child("user").child(receiverUid).child("chatStatus").setValue("1")
                mDbRef.child("user").child(senderUid!!).child("chatStatus").setValue("1")
                mDbRef.child("user").child(senderUid!!).child("lastMsg").setValue(message)
                SendNotification(
                    receiverUid,
                    MyApp.getSharedPref().userName,
                    message,
                    firebaseToken,
                    "1"
                )
            }
        }


        binding.ivSend.setOnClickListener {
            if (binding.etChat.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter your message.", Toast.LENGTH_SHORT).show()
            } else {
                val message = binding.etChat.text.toString()
                val messageObject = Message(message, senderUid, "TEXT", "")
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                mDbRef.child("user").child(receiverUid).child("chatStatus").setValue("1")
                mDbRef.child("user").child(senderUid!!).child("chatStatus").setValue("1")
                mDbRef.child("user").child(receiverUid).child("lastMsg").setValue(message)
                binding.etChat.setText("")
                SendNotification(
                    receiverUid,
                    MyApp.getSharedPref().userName,
                    message,
                    firebaseToken,
                    "1"
                )
            }

        }

        binding.ivVideo.setOnClickListener {
            if (!isPermissionGranted()) {
                askPermissions()
            } else {
                generateRoom()
             /*   startActivity(
                    Intent(this, CreateRoomActivity::class.java)
                        .putExtra("from", "1")
                        .putExtra("clickedUserID", clickedUserId)
                        .putExtra("playdateId", "0")
                        .putExtra("userImage",userImage)
                        .putExtra("name", userName)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))*/
            }
        }

        binding.ivAudio.setOnClickListener {
            if (!isPermissionGranted()) {
                askPermissions()
            } else {
                generateRoom()
               /* startActivity(
                    Intent(this, CreateRoomActivity::class.java)
                        .putExtra("from", "2")
                        .putExtra("clickedUserID", clickedUserId)
                        .putExtra("playdateId", "0")
                        .putExtra("userImage",userImage)
                        .putExtra("name", userName)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))*/
            }
        }

    }


    private fun askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestcode)
    }

    private fun isPermissionGranted(): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }

        return true
    }

    private fun SendNotification(
        senderId: String?,
        userName: String,
        message: String,
        firebaseToken: String,
        code:String
    ) {
        val data = Data(
            senderUid,
            message,
            "New Message From: ${userName}",
            receiverUid,
            "chat",
            MyApp.getSharedPref().userImage,
            code
        )
        val sender = Sender(data, firebaseToken)
        apiserviceshit!!.sendNotification(sender).enqueue(object :
            Callback<com.bnb.doggydoo.firebaseChat.Notification.Response> {

            override fun onFailure(
                call: Call<com.bnb.doggydoo.firebaseChat.Notification.Response>,
                t: Throwable
            ) {
            }

            override fun onResponse(
                call: Call<com.bnb.doggydoo.firebaseChat.Notification.Response>,
                response: Response<com.bnb.doggydoo.firebaseChat.Notification.Response>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.success != 1) {
                        System.out.println("failed")
                    }
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uri = data?.data!!
            imageDialog(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun imageDialog(imagePath: Uri?) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_post_image)

        val imageMessage = dialog.findViewById<View>(R.id.iv_Message) as ImageView
        val yesButton = dialog.findViewById<View>(R.id.btYes) as Button
        val noButton = dialog.findViewById<View>(R.id.btNo) as Button

        imageMessage.setImageURI(imagePath)

        yesButton.setOnClickListener {
            reviewImage()
            imageMessage.setImageResource(0)
            dialog.dismiss()
        }

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun reviewImage() {
        // to Upload Image to firebase storage to get url image...
        if (uri != null) {
            binding.llProgress.visibility = View.VISIBLE
            binding.tvAlert.text = "Uploading image..."
            FirebaseService(this).uploadImageToFireBaseStorage(
                uri!!,
                object : FirebaseService.OnCallBack {
                    override fun onUploadSuccess(imageUrl: String?) {
                        // to send chat image//
                        binding.llProgress.visibility = View.GONE
                        val messageObject = Message(
                            "",
                            senderUid,
                            "IMAGE",
                            imageUrl
                        )
                        mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener {
                                mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                                    .setValue(messageObject)
                            }
                        binding.etChat.setText("")
                        mDbRef.child("user").child(receiverUid).child("chatStatus").setValue("1")
                        mDbRef.child("user").child(senderUid!!).child("chatStatus").setValue("1")
                        mDbRef.child("user").child(senderUid!!).child("lastMsg").setValue("image")
                        SendNotification(
                            receiverUid,
                            MyApp.getSharedPref().userName,
                            "Image",
                            firebaseToken,
                            "1"
                        )
                    }

                    override fun onUploadFailed(e: Exception?) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
    ///////////////////Calling

    private fun generateRoom() {
        homeViewModel.getToken("0", "2")
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }
                        refNo = it.data.refrence_id
                        roomId = it.data.room_id
                        type = it.data.type
                        generateToken(roomId, refNo, "0", type)

                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }


    private fun generateToken(roomId: String, refNo: String, clickedUserId: String, type: String) {
        System.out.println("data is>>" + MyApp.getSharedPref().userName)
        System.out.println("data is>>" + refNo)
        System.out.println("data is>>" + roomId)
        System.out.println("data is>>" + clickedUserId)
        System.out.println("data is>>" + type)
        homeViewModel.getGenToken(
            MyApp.getSharedPref().userName,
            refNo,
            roomId,
            clickedUserId,
            type,
            MyApp.getSharedPref().userId
        )
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()

                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        } else {
                            tokenIs = it.data.token
                            successNavigation(tokenIs, type)
                        }


                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }

    private fun successNavigation(tokenIs: String, type: String) {
        startActivity(
            Intent(
                this, VideoConferenceActivity::class.java
            )
                .putExtra("token", tokenIs)
                .putExtra("name", "user")
                .putExtra("request_type", type)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }

}