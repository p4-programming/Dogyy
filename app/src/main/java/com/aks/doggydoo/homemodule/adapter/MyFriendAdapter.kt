package com.aks.doggydoo.homemodule.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleMyFriendBinding
import com.aks.doggydoo.homemodule.datasource.model.home.FriendReqList
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.network.ApiConstant

class MyFriendAdapter(
    var context: Context,
    private var callRequestedUserInfo: (requestId: String, reqStatus: String) -> Unit
) :
    RecyclerView.Adapter<MyFriendAdapter.MyFriendViewHolder>(), Filterable {
    var friendList: ArrayList<FriendReqList> = ArrayList()
    var friendListFiltered: ArrayList<FriendReqList> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFriendViewHolder {
        val binding =
            SingleMyFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyFriendViewHolder, position: Int) {
        holder.bindAdoptionData(friendListFiltered[position])
    }

    fun addData(list: List<FriendReqList>) {
        friendList = list as ArrayList<FriendReqList>
        friendListFiltered = friendList
        notifyDataSetChanged()
    }

    inner class MyFriendViewHolder(var binding: SingleMyFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindAdoptionData(data: FriendReqList) {
            binding.apply {

                when (data.type) {
                    "send_request" -> {
                        ivAccept.visibility = View.GONE
                        ivRemove.visibility = View.VISIBLE
                        tvReqDetail.text = "Request Sent"

                        name.text = data.reciever_name
                        friendProfilePicture.loadImageFromString(
                            context,
                            ApiConstant.PROFILE_IMAGE_BASE_URL + data.Reciever_photo
                        )

                        binding.mainLayout.setOnClickListener {
                            context.startActivity(
                                Intent(context, UserProfileActivity::class.java)
                                    .putExtra("viewuserid", data.Reciever_id),
                            )
                        }

                    }
                    "recieve_request" -> {
                        ivAccept.visibility = View.VISIBLE
                        ivRemove.visibility = View.VISIBLE
                        tvReqDetail.text = "Request Received"
                        name.text = data.Sender_Name
                        friendProfilePicture.loadImageFromString(
                            context,
                            ApiConstant.PROFILE_IMAGE_BASE_URL + data.Sender_photo
                        )


                        binding.mainLayout.setOnClickListener {
                            context.startActivity(
                                Intent(context, UserProfileActivity::class.java)
                                    .putExtra("viewuserid", data.Sender_id),
                            )
                        }
                    }
                    "send_accept_list" -> {
                        ivAccept.visibility = View.GONE
                        ivRemove.visibility = View.VISIBLE
                        tvReqDetail.text = "Friend"

                        name.text = data.reciever_name
                        friendProfilePicture.loadImageFromString(
                            context,
                            ApiConstant.PROFILE_IMAGE_BASE_URL + data.Reciever_photo
                        )

                        binding.mainLayout.setOnClickListener {
                            context.startActivity(
                                Intent(context, UserProfileActivity::class.java)
                                    .putExtra("viewuserid", data.Reciever_id),
                            )
                        }

                    }
                    "recieve_accept_list" -> {
                        ivAccept.visibility = View.GONE
                        ivRemove.visibility = View.VISIBLE
                        tvReqDetail.text = "Friends"

                        name.text = data.Sender_Name
                        friendProfilePicture.loadImageFromString(
                            context,
                            ApiConstant.PROFILE_IMAGE_BASE_URL + data.Sender_photo
                        )

                        binding.mainLayout.setOnClickListener {
                            context.startActivity(
                                Intent(context, UserProfileActivity::class.java)
                                    .putExtra("viewuserid", data.Sender_id),
                            )
                        }
                    }
                }
            }

            binding.ivAccept.setOnClickListener {
                callRequestedUserInfo(data.Request_id, "1")
            }

            binding.ivRemove.setOnClickListener {
                if (binding.tvReqDetail.text == "Friends"){
                    callRequestedUserInfo(data.Request_id, "5")
                }else{
                    callRequestedUserInfo(data.Request_id, "2")
                }

            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                friendListFiltered = if (charSearch.isEmpty()) {
                    friendList
                } else {
                    val resultList = ArrayList<FriendReqList>()
                    for (row in friendList) {
                        if (row.Sender_Name.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = friendListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                friendListFiltered = results?.values as ArrayList<FriendReqList>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = friendListFiltered.size
}