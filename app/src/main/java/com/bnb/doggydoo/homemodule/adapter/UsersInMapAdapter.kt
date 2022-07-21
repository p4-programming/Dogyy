package com.bnb.doggydoo.homemodule.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.databinding.SingleSnapUserBinding
import com.bnb.doggydoo.parkdescription.ui.PetParkDescription

class UsersInMapAdapter(private var context: Context) : RecyclerView.Adapter<UsersInMapAdapter.UsersInMapViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersInMapViewHolder {
        val binding =
            SingleSnapUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersInMapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersInMapViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class UsersInMapViewHolder(var binding: SingleSnapUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
init{
    binding.layout.setOnClickListener {
        context.startActivity(Intent(context,PetParkDescription::class.java))
    }
}
    }

}