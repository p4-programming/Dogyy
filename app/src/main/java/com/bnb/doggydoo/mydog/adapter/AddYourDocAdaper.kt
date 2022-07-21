package com.bnb.doggydoo.mydog.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.SingleAddDocBinding
import com.bnb.doggydoo.mydog.ui.PreviewDocument

class AddYourDocAdapter(var context: Context) :
    RecyclerView.Adapter<AddYourDocAdapter.AddDocViewHolder>() {

    private val names = arrayListOf("Camera", "Gallery", "Dropbox", "Google Drive")
    private val images =
        arrayListOf(R.drawable.camera, R.drawable.gallery, R.drawable.dropbox, R.drawable.drive)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddDocViewHolder {
        val binding =
            SingleAddDocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddDocViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: AddDocViewHolder, position: Int) {
        holder.bind(names[position], images[position])
    }

    inner class AddDocViewHolder(var binding: SingleAddDocBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.parent.setOnClickListener {
                context.startActivity(Intent(context, PreviewDocument::class.java))
            }
        }

        fun bind(s: String, i: Int) {
            binding.image.setImageResource(i)
            binding.text.text = s
        }
    }
}