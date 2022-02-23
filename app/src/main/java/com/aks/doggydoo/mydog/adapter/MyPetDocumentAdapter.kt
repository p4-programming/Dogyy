package com.aks.doggydoo.mydog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SinglePetDocumentBinding
import com.aks.doggydoo.mydog.datasource.model.petdescriptionmodel.Documentdetail
import com.aks.doggydoo.utils.network.ApiConstant.PET_DOC_IMAGE_BASE_URL

class MyPetDocumentAdapter(var context: Context) :
    ListAdapter<Documentdetail, MyPetDocumentAdapter.MyPetDocumentViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetDocumentViewHolder {
        val binding =
            SinglePetDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPetDocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPetDocumentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyPetDocumentViewHolder(var binding: SinglePetDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: Documentdetail) {
            binding.docImage.loadImageFromString(
                context,
                PET_DOC_IMAGE_BASE_URL + detail.document
            )
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<Documentdetail>() {
            override fun areItemsTheSame(oldItem: Documentdetail, newItem: Documentdetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Documentdetail,
                newItem: Documentdetail
            ): Boolean = oldItem.equals(newItem)
        }
    }
}