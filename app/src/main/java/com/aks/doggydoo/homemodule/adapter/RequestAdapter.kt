package com.aks.doggydoo.homemodule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.chatMessage.datasource.model.request.ReceiveRequest
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleRequestBinding
import com.aks.doggydoo.mydog.ui.PetProfileActivity
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.network.ApiConstant.PROFILE_IMAGE_BASE_URL

const val SENT = "sentRequest"
const val RECEIVE = "receiveRequest"

class RequestAdapter(
    var context: Context,
    private var callRequestedUserInfo: (requestId: String, reqType: String, reqStatus: String, petId: String,
                                        userId: String, userName:String, userUID:String, userImage:String) -> Unit
) : ListAdapter<ReceiveRequest, RequestAdapter.RequestViewHolder>(DiffCallback()) {
    private var callingFrom: String = RECEIVE
    private var type: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = SingleRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    fun from(from: String) {
        callingFrom = from
    }

    inner class RequestViewHolder(var binding: SingleRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(detail: ReceiveRequest) {
            if (callingFrom == SENT) {
                binding.acceptRequest.hide()
            }

            binding.requestProfilePic.loadImageFromString(
                context,
                PROFILE_IMAGE_BASE_URL + detail.user_image
            )
            binding.profileName.text = detail.user_name
            if (callingFrom == SENT){
                binding.requestType.text = detail.type +" request is sent to "+ detail.pet_name
            }else{
                binding.requestType.text = "New"+" "+detail.type+" to"+" "+detail.pet_name
            }

            binding.day.text = detail.send_date

            binding.acceptRequest.setOnClickListener {
                callRequestedUserInfo(detail.id, detail.type, "1",detail.pet_id, detail.user_id,
                    detail.user_name,detail.userUid,detail.user_image)
            }
            binding.removeRequest.setOnClickListener {
                callRequestedUserInfo(detail.id, detail.type, "2",detail.pet_id,detail.user_id,
                    detail.user_name, "0",detail.user_image)

            }

            binding.requestType.setOnClickListener {
                context.startActivity(Intent(context, PetProfileActivity::class.java)
                    .putExtra("petId",detail.pet_id)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }

            binding.requestProfilePic.setOnClickListener{
                context.startActivity(Intent(context, UserProfileActivity::class.java)
                    .putExtra("viewuserid", detail.user_id)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<ReceiveRequest>() {
            override fun areItemsTheSame(oldItem: ReceiveRequest, newItem: ReceiveRequest) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ReceiveRequest,
                newItem: ReceiveRequest
            ): Boolean = oldItem.equals(newItem)
        }
    }
}
