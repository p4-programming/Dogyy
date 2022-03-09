package com.aks.doggydoo.homemodule.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SinglePlaydateBinding
import com.aks.doggydoo.homemodule.datasource.model.home.ParkPlayDate
import com.aks.doggydoo.playdate.ui.PlayDateDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant.PET_IMAGE_BASE_URL

private const val TAG = "playdateTag"

class HomePlayDateAdapter(var context: Context) :
    ListAdapter<ParkPlayDate, HomePlayDateAdapter.HomePlayDateViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePlayDateViewHolder {
        val binding =
            SinglePlaydateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePlayDateViewHolder(binding)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: HomePlayDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HomePlayDateViewHolder(var binding: SinglePlaydateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(detail: ParkPlayDate) {
            if (absoluteAdapterPosition % 2 == 1) {
                binding.playDateTime.setTextColor(context.getColor(R.color.hint_color))
            }
            binding.apply {
                binding.playDateName.text = detail.pet_name
                binding.playDateTime.text = detail.play_date
                binding.playDateImage.loadImageFromString(
                    context,
                    PET_IMAGE_BASE_URL + detail.pet_image
                )

                mainLayout.setOnClickListener {
                    context.startActivity(
                        Intent(context, PlayDateDetailActivity::class.java)
                            .putExtra("pet_id", detail.petid)
                            .putExtra("from", "upcoming")
                            .putExtra("request_id", detail.request_id)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                }
            }
        }

    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<ParkPlayDate>() {
            override fun areItemsTheSame(oldItem: ParkPlayDate, newItem: ParkPlayDate) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ParkPlayDate,
                newItem: ParkPlayDate
            ): Boolean = oldItem.equals(newItem)
        }
    }
}