package com.bnb.doggydoo.sos.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.FragmentCommentsBinding
import com.bnb.doggydoo.sos.ui.adapter.CommentAdapter
import com.bnb.doggydoo.sos.ui.model.CommentModels
import com.bnb.doggydoo.utils.MyApp
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var DataList: ArrayList<CommentModels>
    private var petId: String = "129"
    private var UserId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserId = MyApp.getSharedPref().userId
        binding.rvComment.layoutManager = LinearLayoutManager(context)
        binding.rvComment.setHasFixedSize(true)
        DataList = arrayListOf()
        getData()
    }

    private fun getData(){
        binding.progressBar.show()
        FirebaseDatabase.getInstance().getReference("comments").child(petId).orderByChild("userID").equalTo(UserId).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.i("TAG", "onDataChange: TEST :: $snapshot")
                    DataList.clear()
                    if (snapshot.exists()){
                        for (userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue(CommentModels::class.java)
                            DataList.add(user!!)
                        }
                        binding.progressBar.hide()
                        val mAdapter = CommentAdapter(context!!,DataList)
                        binding.rvComment.adapter = mAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error $error",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}


