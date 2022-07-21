package com.bnb.doggydoo.playdate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleItemDogsatBinding
import com.bnb.doggydoo.playdate.datasource.model.homepage.UpcomingPlayDates
import com.bnb.doggydoo.playdate.ui.PlayDateDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant

class UpcomingPlaydateAdapter(var context: Context) :
    ListAdapter<UpcomingPlayDates, UpcomingPlaydateAdapter.DogsittingDogsatViewHolder>(
        UpcomingPlaydateAdapter.Companion.DiffCallback()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsittingDogsatViewHolder {
        val binding =
            SingleItemDogsatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogsittingDogsatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogsittingDogsatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DogsittingDogsatViewHolder(var binding: SingleItemDogsatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: UpcomingPlayDates) {
            binding.tvNameAge.text = "${detail.pet_name}, ${detail.pet_age} Years"
            binding.tvBreed.text = detail.breed
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PET_IMAGE_BASE_URL + detail.pet_image
            )


            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, PlayDateDetailActivity::class.java)
                        .putExtra("pet_id", detail.id)
                        .putExtra("from", "upcoming")
                        //.putExtra("request_id", detail.request_id)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<UpcomingPlayDates>() {
            override fun areItemsTheSame(oldItem: UpcomingPlayDates, newItem: UpcomingPlayDates) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: UpcomingPlayDates,
                newItem: UpcomingPlayDates
            ): Boolean = oldItem.equals(newItem)
        }
    }

}