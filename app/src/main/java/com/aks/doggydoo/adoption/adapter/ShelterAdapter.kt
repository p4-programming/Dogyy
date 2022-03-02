package com.aks.doggydoo.adoption.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.adoptdogdetails.ui.AdoptDogDetailActivity
import com.aks.doggydoo.adoption.datasource.model.ShelterList
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleShelderBinding
import com.aks.doggydoo.shelter.ui.DogShelterActivity
import com.aks.doggydoo.utils.network.ApiConstant.BLOG_IMAGE_BASE_URL


class ShelterAdapter(var context: Context) :
    ListAdapter<ShelterList, ShelterAdapter.ShelterViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelterViewHolder {
        val binding =
            SingleShelderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShelterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShelterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ShelterViewHolder(var binding: SingleShelderBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(detail: ShelterList) {
            binding.name.text = detail.name
            binding.address.text = detail.address
            binding.shelderImage.loadImageFromString(context, BLOG_IMAGE_BASE_URL + detail.pic)


            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        DogShelterActivity::class.java
                    ).putExtra("title", detail.name).putExtra("shelter_id", detail.shelter_id)
                )
                /*context.startActivity(
                    Intent(
                        context,
                        AdoptDogDetailActivity::class.java
                    ).putExtra("adoptId", detail.shelter_id)
                )*/
            }

        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<ShelterList>() {
            override fun areItemsTheSame(oldItem: ShelterList, newItem: ShelterList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ShelterList,
                newItem: ShelterList
            ): Boolean = oldItem.equals(newItem)
        }
    }
}