package com.bnb.doggydoo.parkdescription.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.databinding.SingleOverlapPeopleBinding

class OverlapPeopleAdapter() :
    RecyclerView.Adapter<OverlapPeopleAdapter.OverlapPeopleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverlapPeopleViewHolder {
        val binding =
            SingleOverlapPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OverlapPeopleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OverlapPeopleViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class OverlapPeopleViewHolder(var binding: SingleOverlapPeopleBinding) :
        RecyclerView.ViewHolder(binding.root) {}


}