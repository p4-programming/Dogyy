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
import com.bnb.doggydoo.playdate.datasource.model.homepage.LastPlayDates
import com.bnb.doggydoo.playdate.ui.PlayDateDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant

class LastPlaydateAdapter(var context: Context) :
    ListAdapter<LastPlayDates, LastPlaydateAdapter.DogsittingDogsatViewHolder>(LastPlaydateAdapter.Companion.DiffCallback()) {


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

        fun bind(detail: LastPlayDates) {
            if (detail.pet_age_month == "0") {
                binding.tvNameAge.text = "${detail.pet_name}, ${detail.pet_age} Years"
            } else {
                binding.tvNameAge.text =
                    "${detail.pet_name}, ${detail.pet_age} Years  ${detail.pet_age_month} month"
            }
            binding.tvBreed.text = detail.breed
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PET_IMAGE_BASE_URL + detail.pet_image
            )


            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, PlayDateDetailActivity::class.java).putExtra(
                        "pet_id",
                        detail.pet_id
                    )
                        .putExtra("pet_id", detail.id)
                        .putExtra("from", "last")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<LastPlayDates>() {
            override fun areItemsTheSame(oldItem: LastPlayDates, newItem: LastPlayDates) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: LastPlayDates,
                newItem: LastPlayDates
            ): Boolean = oldItem.equals(newItem)
        }
    }
}