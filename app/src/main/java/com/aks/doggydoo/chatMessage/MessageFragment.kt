package com.aks.doggydoo.chatMessage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.aks.doggydoo.databinding.FragmentMessageBinding
import com.aks.doggydoo.firebaseChat.Message
import com.aks.doggydoo.firebaseChat.User
import com.aks.doggydoo.firebaseChat.adapter.FirebaseUserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userFirebaseList: ArrayList<User>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var adapter: FirebaseUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        userFirebaseList = ArrayList()



        adapter = FirebaseUserAdapter(requireContext(), userFirebaseList)
        binding.rvMessage.adapter = adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userFirebaseList.clear()

                for (postSnapshot in snapshot.children) {

                    val currentUser = postSnapshot.getValue(User::class.java)
                    Toast.makeText(requireActivity(), currentUser.toString(), Toast.LENGTH_SHORT).show()



                     val senderRoom = currentUser?.uid + Firebase.auth.currentUser?.uid
                    mDbRef.child("chats").child(senderRoom).child("messages")
                        .addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {

                                if (mAuth.currentUser?.uid != currentUser?.uid) {
                                    userFirebaseList.add(currentUser!!)

                                }

                            }
                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                }

                binding.progressBar.visibility = View.GONE
                adapter.notifyDataSetChanged()
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