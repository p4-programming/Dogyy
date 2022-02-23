package com.aks.doggydoo.chatMessage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.databinding.SingleCallItemBinding

class CallsAdapter (var context: Context) : RecyclerView.Adapter<CallsAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = SingleCallItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RequestViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {

    }

    inner class RequestViewHolder(var binding: SingleCallItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}