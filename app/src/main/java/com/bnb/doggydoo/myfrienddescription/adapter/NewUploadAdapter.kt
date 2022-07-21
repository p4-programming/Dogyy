package com.bnb.doggydoo.myfrienddescription.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleFriendNewUploadBinding
import com.bnb.doggydoo.myprofile.datasource.model.profile.NewUploadData
import com.bnb.doggydoo.newsfeed.ui.ArticleDetailsActivity
import com.bnb.doggydoo.utils.network.ApiConstant.BLOG_IMAGE_BASE_URL

class NewUploadAdapter(var context: Context) :
    ListAdapter<NewUploadData, NewUploadAdapter.NewUploadViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewUploadViewHolder {
        val binding = SingleFriendNewUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewUploadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewUploadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewUploadViewHolder(var binding: SingleFriendNewUploadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: NewUploadData) {
            binding.docUpoloadedImage.loadImageFromString(
                context,
                BLOG_IMAGE_BASE_URL + detail.file
            )

            binding.tvLike.text = detail.like
            binding.tvComment.text = detail.commentcount

            binding.docUpoloadedImage.setOnClickListener {
                context.startActivity(
                    Intent(context, ArticleDetailsActivity::class.java)
                        .putExtra("type", "image")
                        .putExtra("url", BLOG_IMAGE_BASE_URL + detail.file)
                        .putExtra("caption", "")
                        .putExtra("description", "")
                        .putExtra("likeCount", "0")
                        .putExtra("isLiked", detail.like)
                        .putExtra("commentCount", detail.commentcount)
                        .putExtra("newsfeedId", detail.id)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }

        }

           }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NewUploadData>() {
            override fun areItemsTheSame(oldItem: NewUploadData, newItem: NewUploadData) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NewUploadData,
                newItem: NewUploadData
            ): Boolean = oldItem.equals(newItem)
        }
    }
}