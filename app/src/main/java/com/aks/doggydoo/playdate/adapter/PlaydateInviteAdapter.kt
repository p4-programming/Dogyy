package com.aks.doggydoo.playdate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemDogsatBinding
import com.aks.doggydoo.playdate.datasource.model.homepage.InvitesPlayDates
import com.aks.doggydoo.playdate.ui.PlayDateDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant

class PlaydateInviteAdapter(var context: Context) :
    ListAdapter<InvitesPlayDates, PlaydateInviteAdapter.DogsittingDogsatViewHolder>(
        PlaydateInviteAdapter.Companion.DiffCallback()
    ) {


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

        fun bind(detail: InvitesPlayDates) {
            binding.tvNameAge.text = "${detail.pet_name}, ${detail.pet_age} Years"
            binding.tvBreed.text = detail.breed
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PET_IMAGE_BASE_URL + detail.pet_image
            )


            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, PlayDateDetailActivity::class.java)
                        .putExtra("pet_id", detail.id)
                        .putExtra("from", "invite")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<InvitesPlayDates>() {
            override fun areItemsTheSame(oldItem: InvitesPlayDates, newItem: InvitesPlayDates) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: InvitesPlayDates,
                newItem: InvitesPlayDates
            ): Boolean = oldItem.equals(newItem)
        }
    }

}