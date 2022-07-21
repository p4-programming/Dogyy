package com.bnb.doggydoo.dogsitting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleItemHerosBinding
import com.bnb.doggydoo.dogsitting.datasource.model.DogSitHerosList
import com.bnb.doggydoo.dogsitting.ui.DogSittingHerosDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant

class DogsittingHerosAdapter(var context: Context) :
    ListAdapter<DogSitHerosList, DogsittingHerosAdapter.DogsittingHerosViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsittingHerosViewHolder {
        val binding =
            SingleItemHerosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogsittingHerosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogsittingHerosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DogsittingHerosViewHolder(var binding: SingleItemHerosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: DogSitHerosList) {
            binding.tvNameAge.text = detail.uname
            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PROFILE_IMAGE_BASE_URL + detail.profile_pic
            )

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, DogSittingHerosDetailActivity::class.java)
                        .putExtra("dogsitHeroId", detail.id))
                   /* Intent(context, UserProfileActivity::class.java)
                        .putExtra("viewuserid", detail.id))*/
            }
        }
    }
    companion object {
        class DiffCallback : DiffUtil.ItemCallback<DogSitHerosList>() {
            override fun areItemsTheSame(oldItem: DogSitHerosList, newItem: DogSitHerosList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DogSitHerosList,
                newItem: DogSitHerosList
            ): Boolean = oldItem.equals(newItem)
        }
    }

}