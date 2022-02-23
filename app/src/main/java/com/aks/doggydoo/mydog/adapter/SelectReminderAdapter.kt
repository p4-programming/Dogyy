package com.aks.doggydoo.mydog.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleSelectReminderBinding

class SelectReminderAdapter(
    private var context: Context,
    private var setNewReminderType: (showOtherText: Boolean) -> Unit,
    private var reminderPosition: (reminderPosition: Int) -> Unit
) :
    RecyclerView.Adapter<SelectReminderAdapter.SelectReminderViewHolder>() {

    private val names = arrayListOf(
        "Bathing", "Grooming", "Vet Appointment",
        "Vaccination", "Walk", "Feed", "Other"
    )
    private var selectedItem = 0
    private val images = arrayListOf(
        R.drawable.bath, R.drawable.scissors, R.drawable.hospital,
        R.drawable.sirenge, R.drawable.pawprints, R.drawable.dog_food, R.drawable.star
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectReminderViewHolder {
        val binding = SingleSelectReminderBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return SelectReminderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: SelectReminderViewHolder, position: Int) {
        holder.bind(names[position], images[position])
    }

    inner class SelectReminderViewHolder(var binding: SingleSelectReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.parent.setOnClickListener {
                if (binding.textView.text == "Other") {
                    setNewReminderType(true)
                } else {
                    setNewReminderType(false)
                }
                reminderPosition(absoluteAdapterPosition)
                selectedItem = absoluteAdapterPosition
                notifyDataSetChanged()
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(name: String, image: Int) {
            binding.image.setImageResource(image)
            binding.textView.text = name
            if (selectedItem == absoluteAdapterPosition) {
                binding.tick.show()
                binding.textView.setTextColor(context.resources.getColor(R.color.on_boarding_blue))
                binding.textView.setTextAppearance(R.style.selectedFont)
            } else {
                binding.tick.hide()
                binding.textView.setTextColor(context.resources.getColor(R.color.optionUnselectedcolor))
                binding.textView.setTextAppearance(R.style.unselectedFont)
            }
        }
    }
}