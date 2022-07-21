package com.bnb.doggydoo.newsfeed.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.SingleItemUserBinding
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.newsfeed.datasource.model.NewsFeedCommentDetail
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.network.ApiConstant

class UserAdapter(var context: Context) :
    ListAdapter<NewsFeedCommentDetail, UserAdapter.UserViewHolder>(UserAdapter.Companion.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            SingleItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(var binding: SingleItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsFeedCommentDetail) {
            binding.apply {
                tvAddress.show()
                ProfilePicture.loadImageFromString(context, ApiConstant.PROFILE_IMAGE_BASE_URL + data.userphoto)
                name.text = data.username
                tvMobileNo.text = data.comment
                tvAddress.text = data.createon

                ProfilePicture.setOnClickListener {
                    if (data.user_id != MyApp.getSharedPref().userId){
                        context.startActivity(
                            Intent(context, UserProfileActivity::class.java)
                                .putExtra("viewuserid", data.user_id)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }

                }
            }


        }

    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NewsFeedCommentDetail>() {
            override fun areItemsTheSame(oldItem: NewsFeedCommentDetail, newItem: NewsFeedCommentDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NewsFeedCommentDetail,
                newItem: NewsFeedCommentDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }

}