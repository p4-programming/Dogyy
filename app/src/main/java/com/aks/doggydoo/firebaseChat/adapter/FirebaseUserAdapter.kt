package com.aks.doggydoo.firebaseChat.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.firebaseChat.ChatActivity
import com.aks.doggydoo.firebaseChat.Message
import com.aks.doggydoo.firebaseChat.User
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.network.ApiConstant
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class FirebaseUserAdapter(var context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<FirebaseUserAdapter.UserViewHolder>() {
    private var messageList: ArrayList<Message> = ArrayList()
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

        holder.userLayout.setOnClickListener {
            holder.progressBar.visibility = View.VISIBLE
            senderRoom = currentUser.uid + Firebase.auth.currentUser?.uid

            mDbRef.child("chats").child(senderRoom).child("messages")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (postSnapShot in snapshot.children) {
                            val message = postSnapShot.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            Handler(Looper.getMainLooper()).postDelayed({
                if (messageList.size > 0) {
                    for (i in 0..messageList.size - 1) {
                        if (messageList.get(i).senderId!!.equals(Firebase.auth.currentUser?.uid) || messageList.get(
                                i
                            ).senderId!!.equals(
                                currentUser.uid
                            )
                        ) {
                            context.startActivity(
                                Intent(context, ChatActivity::class.java)
                                    .putExtra("name", currentUser.uname)
                                    .putExtra("uid", currentUser.uid)
                                    .putExtra("clicked_user_id", currentUser.userId)
                                    .putExtra("userImage", currentUser.profilePic)
                                    .putExtra("firebasetoken", currentUser.firebaseToken)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        } else {
                            Toast.makeText(
                                context,
                                "Not allowed to chat with this user now.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    holder.progressBar.visibility = View.GONE
                } else {
                    holder.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "No conversation found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, 3000)


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
    }

}