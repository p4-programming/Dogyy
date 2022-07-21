package com.bnb.doggydoo.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.SingleItemIdBinding
import com.bnb.doggydoo.onboarding.datasource.model.user.UserNameList

class UserIdSuggestionAdapter (var context: Context) :
    ListAdapter<UserNameList, UserIdSuggestionAdapter.BreedViewHolder>(UserIdSuggestionAdapter.Companion.DiffCallback()) {
    var selectedPosition = -1

    inner class BreedViewHolder(var binding: SingleItemIdBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(detail: UserNameList) {
            binding.userName.text = detail.user_name

            if (selectedPosition == absoluteAdapterPosition) {
                binding.userName.setTextColor(context.resources.getColor(R.color.on_boarding_blue))
            } else {
                binding.userName.setTextColor(context.resources.getColor(R.color.black))
            }

            binding.parent.setOnClickListener {
                selectedPosition=absoluteAdapterPosition;
                notifyDataSetChanged();
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val binding =
            SingleItemIdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<UserNameList>() {
            override fun areItemsTheSame(oldItem: UserNameList, newItem: UserNameList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: UserNameList,
                newItem: UserNameList
            ): Boolean = oldItem.equals(newItem)
        }
    }
}