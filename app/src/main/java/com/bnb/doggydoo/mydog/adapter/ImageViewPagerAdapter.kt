package com.bnb.doggydoo.mydog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.ViewpagerItemBinding
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.bnb.doggydoo.utils.network.ApiConstant

class ImageViewPagerAdapter (var context: Context) :
    ListAdapter<PetDescriptionResponse, ImageViewPagerAdapter.ImageViewPagerViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewPagerViewHolder {
        val binding =
            ViewpagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageViewPagerViewHolder(var binding: ViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: PetDescriptionResponse) {
            binding.imageView.loadImageFromString(
                context,
                ApiConstant.PET_DOC_IMAGE_BASE_URL + detail.petImage[absoluteAdapterPosition]
            )
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<PetDescriptionResponse>() {
            override fun areItemsTheSame(oldItem: PetDescriptionResponse, newItem: PetDescriptionResponse) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PetDescriptionResponse,
                newItem: PetDescriptionResponse
            ): Boolean = oldItem.equals(newItem)
        }
    }
}