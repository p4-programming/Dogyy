package com.bnb.doggydoo.dogsitting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleItemDogsatBinding
import com.bnb.doggydoo.dogsitting.adapter.UpcomingDogAdapter
import com.bnb.doggydoo.dogsitting.datasource.model.NearByDogsitPetList
import com.bnb.doggydoo.dogsitting.ui.DogSittingDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant

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