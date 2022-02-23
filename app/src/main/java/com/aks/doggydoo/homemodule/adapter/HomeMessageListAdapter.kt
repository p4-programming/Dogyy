package com.aks.doggydoo.homemodule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.databinding.SingleMessageInHomeBinding
private const val TAG = "msgTag"
class HomeMessageListAdapter() :
    RecyclerView.Adapter<HomeMessageListAdapter.HomeMessageListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMessageListViewHolder {
        return HomeMessageListViewHolder(
            SingleMessageInHomeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: HomeMessageListViewHolder, position: Int) {

    }

    inner class HomeMessageListViewHolder(var binding: SingleMessageInHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}