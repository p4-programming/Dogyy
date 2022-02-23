package com.aks.doggydoo.playdate.adapter

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
import com.aks.doggydoo.playdate.datasource.model.MyPetDetail

class SelectPetAdapter(var context: Context) :
    ListAdapter<MyPetDetail, SelectPetAdapter.BreedViewHolder>(SelectPetAdapter.Companion.DiffCallback()) {
    var selectedPosition = -1

    inner class BreedViewHolder(var binding: SingleBreedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: MyPetDetail) {
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
        class DiffCallback : DiffUtil.ItemCallback<MyPetDetail>() {
            override fun areItemsTheSame(oldItem: MyPetDetail, newItem: MyPetDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MyPetDetail,
                newItem: MyPetDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }

}