package com.aks.doggydoo.adoption.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.adoption.datasource.model.ShelterListData
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleShelderBinding
import com.aks.doggydoo.shelter.ui.DogShelterActivity
import com.aks.doggydoo.utils.network.ApiConstant

class ViewAllShelterAdapter(var context: Context) :
    ListAdapter<ShelterListData, ViewAllShelterAdapter.ViewAllViewHolder>(ViewAllShelterAdapter.Companion.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllViewHolder {
        Log.d("mhsdafdfa", "onCreateViewHolder: ")
        return ViewAllViewHolder(
            SingleShelderBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewAllViewHolder, position: Int) {
        holder.bindAdoptionData(getItem(position))

    }

    inner class ViewAllViewHolder(var binding: SingleShelderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindAdoptionData(data: ShelterListData) {
            binding.apply {
                name.text = data.Name
                address.text = data.address
                shelderImage.loadImageFromString(
                    context,
                    ApiConstant.BLOG_IMAGE_BASE_URL + data.photo
                )

                mainLayout.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DogShelterActivity::class.java
                        ).putExtra("title", data.Name)
                    )
                }
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