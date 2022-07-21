package com.bnb.doggydoo.firebaseChat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.firebaseChat.ChatImageFullActivity
import com.bnb.doggydoo.firebaseChat.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class MessageAdapter(var context: Context, var userName:String, var messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder

            if (currentMessage.messageType =="TEXT"){
                viewHolder.progress.setVisibility(View.GONE)
                viewHolder.sentMsg.text = currentMessage.message
                viewHolder.imageLayout.visibility = View.GONE
                viewHolder.sentMsg.visibility = View.VISIBLE
            }else{
                viewHolder.progress.setVisibility(View.VISIBLE)
                viewHolder.imageLayout.visibility = View.VISIBLE
                viewHolder.sentMsg.visibility = View.GONE
                Picasso.get()
                    .load(currentMessage.imageMessage)
                    .centerCrop()
                    .resize(200,200)
                    .into(viewHolder.sentImage, object : Callback {
                        override fun onSuccess() {
                            viewHolder.progress.setVisibility(View.GONE)
                        }

                        override fun onError(e: Exception?) {
                            viewHolder.progress.setVisibility(View.GONE)
                        }
                    })

                viewHolder.sentImage.setOnClickListener {
                    context.startActivity(
                        Intent(context, ChatImageFullActivity::class.java)
                        .putExtra("userName","You")
                        .putExtra("image",currentMessage.imageMessage)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }

        } else {
            val viewHolder = holder as ReceiveViewHolder

            if (currentMessage.messageType =="TEXT"){
                viewHolder.progress.setVisibility(View.GONE)
                viewHolder.receiveMsg.text = currentMessage.message
                viewHolder.imageLayout.visibility = View.GONE
                viewHolder.receiveMsg.visibility = View.VISIBLE
            }else {
                viewHolder.progress.setVisibility(View.VISIBLE)
                viewHolder.imageLayout.visibility = View.VISIBLE
                viewHolder.receiveMsg.visibility = View.GONE
                Picasso.get()
                    .load(currentMessage.imageMessage)
                    .into(viewHolder.receiveImage, object : Callback {
                        override fun onSuccess() {
                            viewHolder.progress.setVisibility(View.GONE)
                        }

                        override fun onError(e: Exception?) {
                            viewHolder.progress.setVisibility(View.GONE)
                        }
                    })

                viewHolder.receiveImage.setOnClickListener {
                    context.startActivity(Intent(context, ChatImageFullActivity::class.java)
                        .putExtra("userName",userName)
                        .putExtra("image",currentMessage.imageMessage)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (Firebase.auth.currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMsg = itemView.findViewById<TextView>(R.id.txt_send_message)
        val sentImage = itemView.findViewById<ImageView>(R.id.ivSendImage)
        val progress = itemView.findViewById<ProgressBar>(R.id.progress)
        val imageLayout = itemView.findViewById<RelativeLayout>(R.id.rlImageSend)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMsg = itemView.findViewById<TextView>(R.id.txt_receive_message)
        val receiveImage = itemView.findViewById<ImageView>(R.id.ivReceiveImage)
        val progress = itemView.findViewById<ProgressBar>(R.id.progress)
        val imageLayout = itemView.findViewById<RelativeLayout>(R.id.rlImageReceive)
    }
}