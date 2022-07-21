package com.bnb.doggydoo.dogsitting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.SingleBreedItemBinding
import com.bnb.doggydoo.dogsitting.datasource.model.ViewAllPetList

class PetAdapter(var context: Context) :
    ListAdapter<ViewAllPetList, PetAdapter.BreedViewHolder>(PetAdapter.Companion.DiffCallback()) {
    var selectedPosition = -1

    inner class BreedViewHolder(var binding: SingleBreedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: ViewAllPetList) {
            binding.textView.text = detail.pet_name
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
        class DiffCallback : DiffUtil.ItemCallback<ViewAllPetList>() {
            override fun areItemsTheSame(oldItem: ViewAllPetList, newItem: ViewAllPetList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ViewAllPetList,
                newItem: ViewAllPetList
            ): Boolean = oldItem.equals(newItem)
        }
    }

}