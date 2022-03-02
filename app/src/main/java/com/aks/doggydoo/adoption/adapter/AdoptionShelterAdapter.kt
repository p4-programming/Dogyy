package com.aks.doggydoo.adoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.adoption.datasource.model.Pet
import com.aks.doggydoo.adoption.datasource.model.ShelterList
import com.aks.doggydoo.adoption.datasource.model.ShelterListData
import com.aks.doggydoo.adoption.datasource.model.Singlesheltetlist
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleAdoptionBinding
import com.aks.doggydoo.utils.network.ApiConstant

class AdoptionShelterAdapter(
    var context: Context,
    private var callAdoptDogDetail: (view: View, adoptId: String) -> Unit
) :
    ListAdapter<Pet, AdoptionShelterAdapter.AdoptionViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val binding =
            SingleAdoptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdoptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AdoptionViewHolder(var binding: SingleAdoptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mainLayout.setOnClickListener {
                callAdoptDogDetail(binding.dogImage, getItem(absoluteAdapterPosition).pet_id)
            }
        }

        fun bind(data: Pet) {
            binding.apply {
                adoptName.text = data.pet_name
                adoptBreed.text = data.breed
                print("Image URL = " + ApiConstant.BLOG_IMAGE_BASE_URL + data.pet_image)
                dogImage.loadImageFromString(context, ApiConstant.PET_IMAGE_BASE_URL + data.pet_image)
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(oldItem: Pet, newItem: Pet) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Pet,
                newItem: Pet
            ): Boolean = oldItem.equals(newItem)
        }
    }

}