package com.aks.doggydoo.adoptdogdetails.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.callawet.datasource.model.home.NearByDocList
import com.aks.doggydoo.callawet.ui.DoctorDetailActivity
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleAvailableDocBinding
import com.aks.doggydoo.utils.network.ApiConstant

class AvailableDocAdapter(var context: Context) :
    ListAdapter<NearByDocList, AvailableDocAdapter.AvailableDocViewHolder>(AvailableDocAdapter.Companion.DiffCallback())  {
    inner class AvailableDocViewHolder(var binding: SingleAvailableDocBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: NearByDocList) {
            binding.tvName.text = detail.vat_name
            binding.tvAddress.text = detail.vatspecial
            binding.tvDistance.text = "2km"
            binding.homeImage.loadImageFromString(
                context,
                ApiConstant.NEARBY_DOC_IMAGE_BASE_URL + detail.vat_image
            )

            binding.doctorLayout.setOnClickListener {
                context.startActivity(Intent(context, DoctorDetailActivity::class.java)
                    .putExtra("from","nearbyDoc")
                    .putExtra("id",detail.id)
                    .putExtra("image_url",detail.vat_image))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableDocViewHolder {
        val binding = SingleAvailableDocBinding.inflate(LayoutInflater.from(context), parent, false)
        return AvailableDocViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvailableDocViewHolder, position: Int) {
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