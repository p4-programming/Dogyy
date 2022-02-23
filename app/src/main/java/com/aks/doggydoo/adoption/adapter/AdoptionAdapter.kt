package com.aks.doggydoo.adoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.adoption.datasource.model.AdoptionListData
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleAdoptionBinding
import com.aks.doggydoo.utils.network.ApiConstant.PET_IMAGE_BASE_URL

class AdoptionAdapter(var context: Context, private var callAdoptDogDetail: (view: View, adoptId: String) -> Unit) :
    ListAdapter<AdoptionListData, AdoptionAdapter.AdoptionViewHolder>(DiffCallback()) {


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

        fun bind(data: AdoptionListData) {
            binding.apply {
                adoptName.text = "${data.name}, ${data.age}"
                adoptBreed.text = "${data.breed}"

                try {
                    if (data.pic =="user.png"){
                        dogImage.setImageResource(R.drawable.dummy_pet)
                    }else{
                        dogImage.loadImageFromString(context, PET_IMAGE_BASE_URL + data.pic)
                    }
                }catch (e: Exception){

                }
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<AdoptionListData>() {
            override fun areItemsTheSame(oldItem: AdoptionListData, newItem: AdoptionListData) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AdoptionListData,
                newItem: AdoptionListData
            ): Boolean = oldItem.equals(newItem)
        }
    }

}