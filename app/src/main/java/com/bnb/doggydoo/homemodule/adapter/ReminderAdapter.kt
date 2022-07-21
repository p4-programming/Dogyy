package com.bnb.doggydoo.homemodule.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.convertTimeIn12HrFormat
import com.bnb.doggydoo.databinding.SingleDogReminderBinding
import com.bnb.doggydoo.homemodule.datasource.model.home.UserReminderList

class ReminderAdapter (var context: Context) :
    ListAdapter<UserReminderList, ReminderAdapter.MyPetReminderViewHolder>(DiffCallback()) {

    private val images = arrayListOf(
        R.drawable.bath,
        R.drawable.scissors,
        R.drawable.hospital,
        R.drawable.sirenge,
        R.drawable.pawprints,
        R.drawable.dog_food,
        R.drawable.star
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPetReminderViewHolder {
        val binding =
            SingleDogReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPetReminderViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyPetReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyPetReminderViewHolder(var binding: SingleDogReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(detail: UserReminderList) {
            if (!detail.create.isEmpty()) {
                val splitDateAndTime = detail.create.split(" ")
                val date = splitDateAndTime[0]
                val time = splitDateAndTime[1]
                val getTimeIn12HrFormat = time.convertTimeIn12HrFormat()

                binding.time.text = "$getTimeIn12HrFormat"
                binding.date.text = "$date"
                binding.type.text = detail.Type
                binding.petName.text = detail.Pet_name

                when (detail.Type) {
                    "Bathing" -> binding.image.setImageResource(images[0])
                    "bathing" -> binding.image.setImageResource(images[0])
                    "Grooming" -> binding.image.setImageResource(images[1])
                    "grooming" -> binding.image.setImageResource(images[1])
                    "Vet Appointment" -> binding.image.setImageResource(images[2])
                    "Vaccination" -> binding.image.setImageResource(images[3])
                    "vaccination" -> binding.image.setImageResource(images[3])
                    "Walk" -> binding.image.setImageResource(images[4])
                    "walk" -> binding.image.setImageResource(images[4])
                    "Feed" -> binding.image.setImageResource(images[5])
                    "feed" -> binding.image.setImageResource(images[5])
                    "Other" -> binding.image.setImageResource(images[6])
                    "other" -> binding.image.setImageResource(images[6])
                    else ->{
                        binding.image.setImageResource(images[6])
                    }
                }

            }
        }
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<UserReminderList>() {
            override fun areItemsTheSame(oldItem: UserReminderList, newItem: UserReminderList) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: UserReminderList,
                newItem: UserReminderList
            ): Boolean = oldItem.equals(newItem)
        }
    }
}