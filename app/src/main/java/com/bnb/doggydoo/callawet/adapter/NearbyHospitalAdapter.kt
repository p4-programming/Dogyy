package com.bnb.doggydoo.callawet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.callawet.datasource.model.home.NearByHospitalList
import com.bnb.doggydoo.callawet.ui.DoctorDetailActivity
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.databinding.SingleItemNearbyhospitalBinding
import com.bnb.doggydoo.utils.network.ApiConstant

class NearbyHospitalAdapter (var context: Context) :
    ListAdapter<NearByHospitalList, NearbyHospitalAdapter.NearbyDoctorViewHolder>(NearbyHospitalAdapter.Companion.DiffCallback()) {

    inner class NearbyDoctorViewHolder(var binding: SingleItemNearbyhospitalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: NearByHospitalList) {
            binding.tvHospitalName.text = detail.name
            binding.tvDistance.text = detail.km + " "+"Km"
            binding.ivHospital.loadImageFromString(context, ApiConstant.NEARBY_HOS_IMAGE_BASE_URL + detail.place_image)

            binding.main.setOnClickListener {
                context.startActivity(Intent(context, DoctorDetailActivity::class.java)
                    .putExtra("from","nearbyHos")
                    .putExtra("id",detail.id)
                    .putExtra("image_url",detail.place_image))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyDoctorViewHolder {
        val binding = SingleItemNearbyhospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NearbyDoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyDoctorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<NearByHospitalList>() {
            override fun areItemsTheSame(oldItem: NearByHospitalList, newItem: NearByHospitalList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NearByHospitalList,
                newItem: NearByHospitalList
            ): Boolean = oldItem.equals(newItem)
        }
    }
}