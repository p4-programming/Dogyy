package com.aks.doggydoo.fostering.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemHerosBinding
import com.aks.doggydoo.fostering.datasource.model.NearByFosterHeroList
import com.aks.doggydoo.fostering.ui.FosteringHerosDetailActivity
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.network.ApiConstant

class FosterHerosAdapter(var context: Context) :
    ListAdapter<NearByFosterHeroList, FosterHerosAdapter.DogsittingHerosViewHolder>(FosterHerosAdapter.Companion.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsittingHerosViewHolder {
        val binding = SingleItemHerosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogsittingHerosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogsittingHerosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class DogsittingHerosViewHolder(var binding: SingleItemHerosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: NearByFosterHeroList) {
            binding.tvNameAge.text = detail.uname
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PROFILE_IMAGE_BASE_URL + detail.profile_pic
            )

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                   /* Intent(context, FosteringHerosDetailActivity::class.java)
                        .putExtra("viewuserid", detail.id)*/

                    Intent(context, FosteringHerosDetailActivity::class.java)
                        .putExtra("fostertHeroId", detail.id))

            }
        }
    }
    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NearByFosterHeroList>() {
            override fun areItemsTheSame(oldItem: NearByFosterHeroList, newItem: NearByFosterHeroList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NearByFosterHeroList,
                newItem: NearByFosterHeroList
            ): Boolean = oldItem.equals(newItem)
        }
    }

}