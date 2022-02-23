package com.aks.doggydoo.shelter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.databinding.SingleHelpingHandBinding

class HelpingHandAdapter() : RecyclerView.Adapter<HelpingHandAdapter.HelpingHandViewHolder>() {
    inner class HelpingHandViewHolder(var binding: SingleHelpingHandBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpingHandViewHolder {
        val binding =
            SingleHelpingHandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelpingHandViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: HelpingHandViewHolder, position: Int) {

    }
}