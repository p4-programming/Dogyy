package com.bnb.doggydoo.firebaseChat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.firebaseChat.Message
import com.bnb.doggydoo.firebaseChat.User
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.utils.network.ApiConstant
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class FirebaseUserAdapter(var context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<FirebaseUserAdapter.UserViewHolder>() {
    private var mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var senderRoom: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.single_message_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.txtName.text = currentUser.uname
        Picasso.get().load(ApiConstant.PROFILE_IMAGE_BASE_URL + currentUser.profilePic)
            .into(holder.userImage)

        holder.userImage.setOnClickListener {
            context.startActivity(Intent(context, UserProfileActivity::class.java)
                .putExtra("viewuserid",currentUser.userId)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        senderRoom = currentUser.uid + Firebase.auth.currentUser?.uid
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapShot in snapshot.children) {
                        val message = postSnapShot.getValue(Message::class.java)
                        if (message!!.senderId!! == Firebase.auth.currentUser?.uid || message.senderId!! == currentUser.uid) {
                            if (currentUser.lastMsg.isNullOrEmpty()){
                                holder.tvLastMsg.text = "No message"
                            }else{
                                holder.tvLastMsg.text = currentUser.lastMsg.toString()
                            }

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        holder.userLayout.setOnClickListener {
            senderRoom = currentUser.uid + Firebase.auth.currentUser?.uid
            context.startActivity(
                Intent(context, ChatActivity::class.java)
                    .putExtra("name", currentUser.uname)
                    .putExtra("uid", currentUser.uid)
                    .putExtra("clicked_user_id", currentUser.userId)
                    .putExtra("userImage", currentUser.profilePic)
                    .putExtra("firebasetoken", currentUser.firebaseToken)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.profileName)
        val userImage = itemView.findViewById<ImageView>(R.id.requestProfilePic)
        val mainLayout = itemView.findViewById<RelativeLayout>(R.id.mainLayout)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress)
        val userLayout = itemView.findViewById<LinearLayout>(R.id.descLayout)
        val tvLastMsg = itemView.findViewById<TextView>(R.id.tvLastMsg)
    }

}