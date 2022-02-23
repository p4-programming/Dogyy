package com.aks.doggydoo.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleBreedItemBinding
import com.aks.doggydoo.onboarding.datasource.model.pet.PetBreedDetail

class BreedAdapter(var context: Context) :
    ListAdapter<PetBreedDetail, BreedAdapter.BreedViewHolder>(BreedAdapter.Companion.DiffCallback()) {
    var selectedPosition = -1

    inner class BreedViewHolder(var binding: SingleBreedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: PetBreedDetail) {
            binding.textView.text = detail.category.trim()
            if (selectedPosition == absoluteAdapterPosition) {
                binding.tick.show()
                binding.textView.setTextColor(context.resources.getColor(R.color.on_boarding_blue))
            } else {
                binding.tick.hide()
                binding.textView.setTextColor(context.resources.getColor(R.color.black))
            }

            binding.parent.setOnClickListener {
                selectedPosition=absoluteAdapterPosition;
                notifyDataSetChanged();
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val binding =
            SingleBreedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<PetBreedDetail>() {
            override fun areItemsTheSame(oldItem: PetBreedDetail, newItem: PetBreedDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PetBreedDetail,
                newItem: PetBreedDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }

}