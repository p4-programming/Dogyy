package com.bnb.doggydoo.newsfeed.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.articledescription.datasource.model.NewsfeedCommentDetail
import com.bnb.doggydoo.commonutility.convertTimeIn12HrFormat
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.ordinalNumber
import com.bnb.doggydoo.databinding.SingleCommentBinding
import com.bnb.doggydoo.utils.network.ApiConstant.PROFILE_IMAGE_BASE_URL
import java.time.format.DateTimeFormatter

class UserCommentAdapter(var context: Context) :
    ListAdapter<NewsfeedCommentDetail, UserCommentAdapter.UserCommentViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCommentViewHolder {
        val binding =
            SingleCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserCommentViewHolder(binding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserCommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserCommentViewHolder(var binding: SingleCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(detail: NewsfeedCommentDetail) {
            val splitDateAndTime = detail.createon.split(" ")
            val date = splitDateAndTime[0]
            val time = splitDateAndTime[1]
            val getTimeIn12HrFormat = time.convertTimeIn12HrFormat()
            if (date!=("0000-00-00")){
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val newDateFormat = formatter.parse(date)
                val desiredFormat = DateTimeFormatter.ofPattern("dd MMM yyyy").format(newDateFormat)
                val onlyDate = desiredFormat.substring(0, 2)
                val finalDate = onlyDate.toInt().ordinalNumber()
                binding. createdOn.text="$finalDate ${
                    desiredFormat.substring(
                        2,
                        desiredFormat.length
                    )
                }, $getTimeIn12HrFormat"
            }else{
                binding.createdOn.text="empty date"
            }


            binding.apply {
                ivUserImage.loadImageFromString(context, PROFILE_IMAGE_BASE_URL + detail.userphoto)
                tvUserName.text = detail.username
                comment.text = detail.comment

            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NewsfeedCommentDetail>() {
            override fun areItemsTheSame(
                oldItem: NewsfeedCommentDetail,
                newItem: NewsfeedCommentDetail
            ) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NewsfeedCommentDetail,
                newItem: NewsfeedCommentDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }
}