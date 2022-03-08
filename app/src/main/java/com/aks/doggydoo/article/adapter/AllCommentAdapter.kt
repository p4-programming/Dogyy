package com.aks.doggydoo.article.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.article.datasource.model.BlogCommentList
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleItemUserBinding
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.network.ApiConstant

class AllCommentAdapter (var context: Context) :
    ListAdapter<BlogCommentList, AllCommentAdapter.UserViewHolder>(AllCommentAdapter.Companion.DiffCallback()) {

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
        fun bind(data: BlogCommentList) {
            binding.apply {
                tvAddress.show()
                ProfilePicture.loadImageFromString(context, ApiConstant.PROFILE_IMAGE_BASE_URL + data.userphoto)
                name.text = data.username
                tvMobileNo.text = data.comment
                tvAddress.text = data.createdate

                ProfilePicture.setOnClickListener {
                    if (data.userid != MyApp.getSharedPref().userId){
                        context.startActivity(
                            Intent(context, UserProfileActivity::class.java)
                                .putExtra("viewuserid", data.userid)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                }
            }


        }

    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<BlogCommentList>() {
            override fun areItemsTheSame(oldItem: BlogCommentList, newItem: BlogCommentList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: BlogCommentList,
                newItem: BlogCommentList
            ): Boolean = oldItem.equals(newItem)
        }
    }

}