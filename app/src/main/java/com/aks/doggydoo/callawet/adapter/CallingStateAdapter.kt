package com.aks.doggydoo.callawet.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.callawet.datasource.model.callreason.ReasonDetailList
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleCallingStateBinding

class CallingStateAdapter(var context: Context) :
    ListAdapter<ReasonDetailList, CallingStateAdapter.CallingStateViewHolder>(CallingStateAdapter.Companion.DiffCallback()) {
    private var selectedItem = 0
    var onItemClick: ((String)->Unit) ?= null

    inner class CallingStateViewHolder(var binding: SingleCallingStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            onItemClick?.invoke(binding.reason.text.toString())
            binding.mainLayout.setOnClickListener {
                selectedItem = absoluteAdapterPosition
                onItemClick?.invoke(binding.reason.text.toString())
                notifyDataSetChanged()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(detail: ReasonDetailList) {
            binding.reason.text = detail.reason

            if (selectedItem == absoluteAdapterPosition) {
                binding.tick.show()
                binding.reason.setTextColor(context.resources.getColor(R.color.on_boarding_blue))
                binding.reason.setTextAppearance(R.style.selectedFont)
            } else {
                binding.tick.hide()
                binding.reason.setTextColor(context.resources.getColor(R.color.optionUnselectedcolor))
                binding.reason.setTextAppearance(R.style.unselectedFont)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallingStateViewHolder {
        val binding = SingleCallingStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CallingStateViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CallingStateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    companion object {
        class DiffCallback : DiffUtil.ItemCallback<ReasonDetailList>() {
            override fun areItemsTheSame(oldItem: ReasonDetailList, newItem: ReasonDetailList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ReasonDetailList,
                newItem: ReasonDetailList
            ): Boolean = oldItem.equals(newItem)
        }
    }

  /*  fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, absoluteAdapterPosition, getItemViewType())
        }
        return this
    }*/

}