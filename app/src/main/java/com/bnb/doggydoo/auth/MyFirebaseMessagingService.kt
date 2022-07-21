package com.bnb.doggydoo.auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bnb.doggydoo.R
import com.bnb.doggydoo.chatMessage.ChatMessageRequestActivity
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.firebaseChat.Notification.OreaNotification
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.videocall.ReceiverActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var intent: Intent? = null
    private var titlebody: String = ""
    //val sound: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.ringtone)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {

            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val sented = remoteMessage.data.get("sent")
            val msg: String = remoteMessage.data.get("body").toString()
            val notificationType: String = remoteMessage.data.get("type").toString()
            val userId: String = remoteMessage.data.get("sender_id").toString()
            val refId: String = remoteMessage.data.get("refrence_id").toString()
            val roomId: String = remoteMessage.data.get("room_id").toString()
            val userImage: String = remoteMessage.data.get("profile_pic").toString()

            if (notificationType == "chat") {
                val firebaseUser = Firebase.auth.currentUser
                if (firebaseUser != null) {
                    if (sented != null)
                        if (sented == Firebase.auth.currentUser?.uid) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                sendOreoNotification(remoteMessage)
                            } else {
                                sendNotification(remoteMessage)
                            }
                        }
                }

            } else if (notificationType == "Video Request") {
                sendVideoPushNotification(msg, refId, roomId, notificationType, userImage)


                /*  val intent = Intent()
                  intent.action = "some string"
                  //intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                  //intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                  sendBroadcast(intent)*/


            } else if (notificationType == "Audio Request") {
                sendAudioPushNotification(msg, refId, roomId, notificationType, userImage)
            } else {
                sendPushNotification(msg, notificationType, userId)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }


    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendAudioPushNotification(
        messageBody: String,
        refId: String,
        roomId: String,
        notificationType: String,
        userImage: String
    ) {
        intent = Intent(this, ReceiverActivity::class.java)
            .putExtra("roomId", roomId)
            .putExtra("from", "push")
            .putExtra("refId", refId)
            .putExtra("userImage", userImage)
            .putExtra("type", "2")
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val dummyuniqueInt: Int = Random().nextInt(543254)
        val pendingIntent = PendingIntent.getActivity(
            this, dummyuniqueInt, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        // val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val defaultSoundUri: Uri =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.ringtone)
        val contentView = RemoteViews(packageName, R.layout.custom_push)
        //contentView.setImageViewResource(R.id.image, R.drawable.btn_video)
        contentView.setTextViewText(R.id.title, notificationType)
        contentView.setTextViewText(R.id.text, messageBody)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setContent(contentView)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendVideoPushNotification(
        messageBody: String,
        refId: String,
        roomId: String,
        notificationType: String,
        userImage: String
    ) {
        intent = Intent(this, ReceiverActivity::class.java)
            .putExtra("roomId", roomId)
            .putExtra("from", "push")
            .putExtra("refId", refId)
            .putExtra("userImage", userImage)
            .putExtra("type", "1")
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val dummyuniqueInt: Int = Random().nextInt(543254)
        val pendingIntent = PendingIntent.getActivity(
            this, dummyuniqueInt, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri: Uri =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.ringtone)
        //  val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        val contentView = RemoteViews(packageName, R.layout.custom_push)
        //contentView.setImageViewResource(R.id.image, R.drawable.btn_video)
        contentView.setTextViewText(R.id.title, notificationType)
        contentView.setTextViewText(R.id.text, messageBody)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setContent(contentView)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendPushNotification(messageBody: String, reqType: String, userId: String) {
        if (reqType == "Freinds Request") {
            intent = Intent(this, UserProfileActivity::class.java)
                .putExtra("viewuserid", userId)
        } else {
            intent = Intent(this, ChatMessageRequestActivity::class.java)
                .putExtra("from", "request")
        }

        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val dummyuniqueInt: Int = Random().nextInt(543254)
        val pendingIntent = PendingIntent.getActivity(
            this, dummyuniqueInt, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri: Uri =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.ringtone)
        // val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun sendOreoNotification(remoteMessage: RemoteMessage) {
        val user = remoteMessage.data.get("user").toString()
        val title = remoteMessage.data.get("title").toString()
        val body = remoteMessage.data.get("body").toString()
        val userImage = remoteMessage.data.get("userImage").toString()
        val code = remoteMessage.data.get("code").toString()

        val j = user.replace("[\\D]".toRegex(), "").toInt()
        val separated = title.split(":".toRegex()).toTypedArray()
        if (separated.size > 1) {
            titlebody = separated[1]
        } else {
            titlebody = ""
        }

        val bundle = Bundle()
        bundle.putString("friendid", user)
        val sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val predsefits = sharedPreferences.edit()
        predsefits.putString("friendid", user)
        predsefits.apply()
        //  val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //  val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val defaultSound: Uri =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.ringtone)
        val oreoNotification = OreaNotification(this)

        if (code == "1") {
            intent = Intent(this, ChatActivity::class.java)
                .putExtra("name", titlebody)
                .putExtra("uid", user)
                .putExtra("userImage", userImage)
        } else {

        }
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val dummyuniqueInt: Int = Random().nextInt(543254)
        val pendingIntent = PendingIntent.getActivity(
            this, j, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // todo style
        val builder = oreoNotification.getNotificationShit(
            title, body, pendingIntent,
            defaultSound, R.drawable.ic_notification_icon.toString()
        )
        builder.setAutoCancel(true)
        var i = 0
        if (j > 0) {
            i = j
        }
        val shf = getSharedPreferences("NEWPREFS", MODE_PRIVATE)
        val editorSh = shf.edit()
        editorSh.putInt("values", i)
        editorSh.apply()
        oreoNotification.manager.cancelAll()
        oreoNotification.manager.notify(i, builder.build())

    }

    fun sendNotification(remoteMessage: RemoteMessage) {
        val user = remoteMessage.data.get("user").toString()
        val title = remoteMessage.data.get("title").toString()
        val body = remoteMessage.data.get("body").toString()
        val userImage = remoteMessage.data.get("userImage").toString()
        val code = remoteMessage.data.get("code").toString()
        val separated = title.split(":".toRegex()).toTypedArray()
        if (separated.size > 1) {
            titlebody = separated[1]
        } else {
            titlebody = ""
        }

        val j = user.replace("[\\D]".toRegex(), "").toInt()


        val bundle = Bundle()
        bundle.putString("friendid", user)
        val sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val predsefits = sharedPreferences.edit()
        predsefits.putString("friendid", user)
        predsefits.apply()
        val defaultSound: Uri =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.ringtone)
        //  val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//  val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        if (code == "1") {
            intent = Intent(this, ChatActivity::class.java)
                .putExtra("name", titlebody)
                .putExtra("uid", user)
                .putExtra("userImage", userImage)
        } else {

        }


        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, Random().nextInt(543254), intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = getString(R.string.default_notification_channel_id)


        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        val noti = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        noti.cancelAll()
        var i = 0
        if (j > 0) {
            i = j
        }
        noti.notify(i, builder.build())

    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}