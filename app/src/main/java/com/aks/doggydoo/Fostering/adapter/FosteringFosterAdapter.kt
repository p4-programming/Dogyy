package com.aks.doggydoo.fostering.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemDogsatBinding
import com.aks.doggydoo.fostering.datasource.model.NearByFosterPetList
import com.aks.doggydoo.fostering.ui.FosteringDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant

class FosteringFosterAdapter (var context: Context) :
    ListAdapter<NearByFosterPetList, FosteringFosterAdapter.DogsittingDogsatViewHolder>(NearByFosterAdapter.Companion.DiffCallback()) {

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
        fun bind(detail: NearByFosterPetList) {
            binding.tvNameAge.text = detail.name + ","+ " "+ detail.age
            binding.tvBreed.text = detail.breed
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PET_IMAGE_BASE_URL + detail.pic
            )

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, FosteringDetailActivity::class.java)
                        .putExtra("fostertId", detail.foster_id))
            }
        }

    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NearByFosterPetList>() {
            override fun areItemsTheSame(oldItem: NearByFosterPetList, newItem: NearByFosterPetList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NearByFosterPetList,
                newItem: NearByFosterPetList
            ): Boolean = oldItem == newItem
        }
    }
}