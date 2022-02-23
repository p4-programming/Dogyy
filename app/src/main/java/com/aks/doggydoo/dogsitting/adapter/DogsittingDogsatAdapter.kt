package com.aks.doggydoo.dogsitting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemDogsatBinding
import com.aks.doggydoo.dogsitting.adapter.UpcomingDogAdapter
import com.aks.doggydoo.dogsitting.datasource.model.NearByDogsitPetList
import com.aks.doggydoo.dogsitting.ui.DogSittingDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant

class DogsittingDogsatAdapter (var context: Context) :
    ListAdapter<NearByDogsitPetList, DogsittingDogsatAdapter.DogsittingDogsatViewHolder>(UpcomingDogAdapter.Companion.DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsittingDogsatViewHolder {
        val binding = SingleItemDogsatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogsittingDogsatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogsittingDogsatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DogsittingDogsatViewHolder(var binding: SingleItemDogsatBinding) :
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