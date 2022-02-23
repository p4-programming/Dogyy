package com.aks.doggydoo.adoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.adoption.datasource.model.ShelterListData
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleAdoptionBinding
import com.aks.doggydoo.utils.network.ApiConstant

class AdoptionShelterAdapter(
    var context: Context,
    private var callAdoptDogDetail: (view: View, adoptId: String) -> Unit
) :
    ListAdapter<ShelterListData, AdoptionShelterAdapter.AdoptionViewHolder>(DiffCallback()) {


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
                callAdoptDogDetail(binding.dogImage, getItem(absoluteAdapterPosition).id)
            }
        }

        fun bind(data: ShelterListData) {
            binding.apply {
                adoptName.text = data.Name
                adoptBreed.text = data.Breeds
                dogImage.loadImageFromString(context, ApiConstant.BLOG_IMAGE_BASE_URL + data.photo)
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<ShelterListData>() {
            override fun areItemsTheSame(oldItem: ShelterListData, newItem: ShelterListData) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ShelterListData,
                newItem: ShelterListData
            ): Boolean = oldItem.equals(newItem)
        }
    }

}