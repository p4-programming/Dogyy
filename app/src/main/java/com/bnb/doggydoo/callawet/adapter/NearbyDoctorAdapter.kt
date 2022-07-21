package com.bnb.doggydoo.callawet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.callawet.datasource.model.home.NearByDocList
import com.bnb.doggydoo.callawet.ui.DoctorDetailActivity
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleNearbyDocBinding
import com.bnb.doggydoo.utils.network.ApiConstant

class NearbyDoctorAdapter(var context: Context) :
    ListAdapter<NearByDocList, NearbyDoctorAdapter.NearbyDoctorViewHolder>(NearbyDoctorAdapter.Companion.DiffCallback()) {

    inner class NearbyDoctorViewHolder(var binding: SingleNearbyDocBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: NearByDocList) {
            binding.docName.text = detail.vat_name
            binding.tvClinicName.text = detail.vatspecial
            binding.ivDoc.loadImageFromString(
                context,
                ApiConstant.NEARBY_DOC_IMAGE_BASE_URL + detail.vat_image
            )


            binding.main.setOnClickListener {
                context.startActivity(
                    Intent(context, DoctorDetailActivity::class.java)
                        .putExtra("from", "nearbyDoc")
                        .putExtra("id",detail.id)
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyDoctorViewHolder {
        val binding =
            SingleNearbyDocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NearbyDoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyDoctorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NearByDocList>() {
            override fun areItemsTheSame(oldItem: NearByDocList, newItem: NearByDocList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NearByDocList,
                newItem: NearByDocList
            ): Boolean = oldItem.equals(newItem)
        }
    }
}