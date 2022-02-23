package com.aks.doggydoo.dogsitting.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemInvitesBinding
import com.aks.doggydoo.dogsitting.datasource.model.NearByDogsitPetList
import com.aks.doggydoo.dogsitting.ui.DogSittingDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant

class InviteDogAdapter (var context: Context) :
    ListAdapter<NearByDogsitPetList,InviteDogAdapter.InviteDogViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteDogViewHolder {
        val binding =
            SingleItemInvitesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InviteDogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InviteDogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class InviteDogViewHolder(var binding: SingleItemInvitesBinding) :
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