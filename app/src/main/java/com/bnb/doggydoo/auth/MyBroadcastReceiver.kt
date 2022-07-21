package com.bnb.doggydoo.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bnb.doggydoo.videocall.ApplicationController
import com.bnb.doggydoo.videocall.ReceiverActivity


class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show()

        val newAct = Intent(ApplicationController.context, ReceiverActivity::class.java)
        newAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ApplicationController.context.startActivity(newAct)
    }
}