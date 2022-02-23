package com.aks.doggydoo.parkdescription.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SinglePlaceReviewBinding
import com.aks.doggydoo.parkdescription.datasource.model.ParkReviewDetail
import com.aks.doggydoo.utils.network.ApiConstant.PROFILE_IMAGE_BASE_URL

private const val ITEM_COUNT = 3

class PlaceReviewAdapter(private var context: Context) :
    ListAdapter<ParkReviewDetail, PlaceReviewAdapter.PlaceReviewViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceReviewViewHolder {
        val binding =
            SinglePlaceReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceReviewViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlaceReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceReviewViewHolder(var binding: SinglePlaceReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(rateDescription: ParkReviewDetail) {
            val splitDateAndTime = rateDescription.create_date.split(" ")
            val date = splitDateAndTime[0]
            val time = splitDateAndTime[1]

            binding.apply {
                userName.text = rateDescription.userName
                rate.rating = rateDescription.rate.toFloat()
                comment.text = rateDescription.review
                profileImage.loadImageFromString(
                    context,
                    PROFILE_IMAGE_BASE_URL + rateDescription.profilePic
                )
                reviewTime.text = date +" "+time
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<ParkReviewDetail>() {
            override fun areItemsTheSame(oldItem: ParkReviewDetail, newItem: ParkReviewDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ParkReviewDetail,
                newItem: ParkReviewDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }

}