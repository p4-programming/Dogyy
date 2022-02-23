package com.aks.doggydoo.playdate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemDogsatBinding
import com.aks.doggydoo.playdate.datasource.model.homepage.NearbyPlayDates
import com.aks.doggydoo.playdate.ui.PlayDateDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant

class SuitablePetAdapter(var context: Context) :
    ListAdapter<NearbyPlayDates, SuitablePetAdapter.DogsittingDogsatViewHolder>(SuitablePetAdapter.Companion.DiffCallback()) {


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

        fun bind(detail: NearbyPlayDates) {
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
                    ).putExtra("from", "suitable")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NearbyPlayDates>() {
            override fun areItemsTheSame(oldItem: NearbyPlayDates, newItem: NearbyPlayDates) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NearbyPlayDates,
                newItem: NearbyPlayDates
            ): Boolean = oldItem.equals(newItem)
        }
    }


}