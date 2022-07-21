package com.bnb.doggydoo.dogsitting.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleItemNearbyBinding
import com.bnb.doggydoo.dogsitting.datasource.model.NearByDogsitPetList
import com.bnb.doggydoo.dogsitting.ui.DogSittingDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant

class UpcomingDogAdapter(var context: Context) :
    ListAdapter<NearByDogsitPetList,UpcomingDogAdapter.DogsittingViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsittingViewHolder {
        val binding =
            SingleItemNearbyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogsittingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogsittingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DogsittingViewHolder(var binding: SingleItemNearbyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: NearByDogsitPetList) {
            binding.tvNameAge.text = detail.pet_name + ","+ " "+ detail.pet_age
            binding.tvBreed.text = detail.breed
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PET_IMAGE_BASE_URL + detail.pet_image
            )

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, DogSittingDetailActivity::class.java)
                        .putExtra("petid", detail.pet_id))
            }
        }
    }
    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NearByDogsitPetList>() {
            override fun areItemsTheSame(oldItem: NearByDogsitPetList, newItem: NearByDogsitPetList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NearByDogsitPetList,
                newItem: NearByDogsitPetList
            ): Boolean = oldItem.equals(newItem)
        }
    }

}