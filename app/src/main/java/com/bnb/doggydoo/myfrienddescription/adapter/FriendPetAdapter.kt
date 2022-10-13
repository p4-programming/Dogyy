package com.bnb.doggydoo.myfrienddescription.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleFriendPetBinding
import com.bnb.doggydoo.myprofile.datasource.model.pet.MyPetDetail
import com.bnb.doggydoo.utils.network.ApiConstant.PET_IMAGE_BASE_URL

class FriendPetAdapter(
    var context: Context,
    private var callDogDescriptionActivity: (sharedView: View, petId: String) -> Unit
) :
    ListAdapter<MyPetDetail, FriendPetAdapter.FriendPetViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendPetViewHolder {
        val binding =
            SingleFriendPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendPetViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: FriendPetViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.binding.backgroundImage.backgroundTintList =
                context.resources.getColorStateList(R.color.on_boarding_view)
        }
        holder.bind(getItem(position))
    }

    inner class FriendPetViewHolder(var binding: SingleFriendPetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.parentLayout.setOnClickListener {
                callDogDescriptionActivity(
                    binding.petImage,
                    getItem(absoluteAdapterPosition).id
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(detail: MyPetDetail) {
            binding.apply {
                binding.petName.text = detail.pet_name
                binding.petBreed.text = "${detail.breed}, ${detail.pet_age}years ${detail.pet_age_month}months"
                binding.petImage.loadImageFromString(context, PET_IMAGE_BASE_URL + detail.pet_image)
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<MyPetDetail>() {
            override fun areItemsTheSame(oldItem: MyPetDetail, newItem: MyPetDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MyPetDetail,
                newItem: MyPetDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }
}