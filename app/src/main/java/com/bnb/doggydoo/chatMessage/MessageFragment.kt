package com.bnb.doggydoo.chatMessage

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bnb.doggydoo.databinding.FragmentMessageBinding
import com.bnb.doggydoo.firebaseChat.Message
import com.bnb.doggydoo.firebaseChat.User
import com.bnb.doggydoo.firebaseChat.adapter.FirebaseUserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DataSnapshot

import android.util.Log


class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private var userFirebaseList: ArrayList<User> = ArrayList()
    private var userList: ArrayList<User> = ArrayList()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var adapter: FirebaseUserAdapter
    private var senderRoom: String = ""
    private var messageList: ArrayList<Message> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessageBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
    }

    private fun getInit() {
        binding.progressBar.visibility = View.VISIBLE

        mAuth = Firebase.auth
        mDbRef = FirebaseDatabase.getInstance().reference

        Log.d("TAG", "getItemCount: "+ userFirebaseList.size)
        adapter = FirebaseUserAdapter(requireContext(), userFirebaseList)
        binding.rvMessage.adapter = adapter



        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userFirebaseList.clear()
                for (userSnapshot in snapshot.children) {
                    val currentUser = userSnapshot.getValue(User::class.java)

                    senderRoom = currentUser?.uid + Firebase.auth.currentUser?.uid
                    mDbRef.child("chats").child(senderRoom).child("messages")
                        .addValueEventListener(object : ValueEventListener {

                            @SuppressLint("NotifyDataSetChanged")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                messageList.clear()
                                for (postSnapShot in snapshot.children) {
                                    val message = postSnapShot.getValue(Message::class.java)
                                    messageList.add(message!!)
                                }


                                if (messageList.size > 0) {
                                    for (userMessage in messageList) {
                                        if (userMessage.senderId!! == Firebase.auth.currentUser?.uid || userMessage.senderId!! == currentUser?.uid) {
                                            if (mAuth.currentUser?.uid != currentUser?.uid) {
                                                var isPresent = false
                                                for(i in userFirebaseList){
                                                    if(i.uid.equals(currentUser?.uid)){
                                                        isPresent=true
                                                    }
                                                }
                                                if(!isPresent){
                                                    userFirebaseList.add(currentUser!!)
                                                }
                                            }
                                        }
                                    }
                                }
                                Log.d("TAG", "getItemCount: "+ userFirebaseList.size)
                                adapter.notifyDataSetChanged()



                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
            }


        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
