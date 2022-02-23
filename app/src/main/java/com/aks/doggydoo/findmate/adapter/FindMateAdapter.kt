package com.aks.doggydoo.findmate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.adoptdogdetails.ui.AdoptDogDetailActivity
import com.aks.doggydoo.databinding.SingleFindMateBinding

class FindMateAdapter(var context: Context) :
    RecyclerView.Adapter<FindMateAdapter.FindMateViewHolder>() {

    inner class FindMateViewHolder(var binding: SingleFindMateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.dogImage2.setOnClickListener {
                context.startActivity(Intent(context, AdoptDogDetailActivity::class.java)
                    .putExtra("from","findAMate"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindMateViewHolder {
        val binding =
            SingleFindMateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FindMateViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: FindMateViewHolder, position: Int) {

    }
}