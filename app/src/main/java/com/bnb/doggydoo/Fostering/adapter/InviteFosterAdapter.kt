package com.bnb.doggydoo.fostering.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleItemInvitesBinding
import com.bnb.doggydoo.fostering.datasource.model.NearByFosterPetList
import com.bnb.doggydoo.fostering.ui.FosteringDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant

class InviteFosterAdapter (var context: Context) :
    ListAdapter<NearByFosterPetList,InviteFosterAdapter.InviteDogViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteDogViewHolder {
        val binding =
            SingleItemInvitesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InviteDogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InviteDogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class InviteDogViewHolder(var binding: SingleItemInvitesBinding) :
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
            ): Boolean = oldItem.equals(newItem)
        }
    }
}