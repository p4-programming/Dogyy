package com.aks.doggydoo.homemodule.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.convertTimeIn12HrFormat
import com.aks.doggydoo.databinding.SingleItemReminderBinding
import com.aks.doggydoo.homemodule.datasource.model.home.UserReminderList

class ViewAllReminderAdapter (var context: Context) :
    RecyclerView.Adapter<ViewAllReminderAdapter.ViewAllPlayDateViewHolder>(), Filterable {

    var petList: ArrayList<UserReminderList> = ArrayList()
    var petListFiltered: ArrayList<UserReminderList> = ArrayList()
    private val images = arrayListOf(
        R.drawable.bath,
        R.drawable.scissors,
        R.drawable.hospital,
        R.drawable.sirenge,
        R.drawable.pawprints,
        R.drawable.dog_food,
        R.drawable.star
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllPlayDateViewHolder {
        val binding =
            SingleItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewAllPlayDateViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewAllPlayDateViewHolder, position: Int) {
        holder.bind(petListFiltered[position])
    }

    fun addData(list: List<UserReminderList>) {
        petList = list as ArrayList<UserReminderList>
        petListFiltered = petList
        notifyDataSetChanged()
    }

    inner class ViewAllPlayDateViewHolder(var binding: SingleItemReminderBinding) :
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
                }

            }
        }
    }

    override fun getItemCount(): Int = petListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) petListFiltered = petList else {
                    val filteredList = ArrayList<UserReminderList>()
                    petList
                        .filter {
                            (it.Type.contains(constraint!!)) or
                                    (it.create.contains(constraint))

                        }
                        .forEach { filteredList.add(it) }
                    petListFiltered = filteredList

                }
                return FilterResults().apply { values = petListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                petListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<UserReminderList>
                notifyDataSetChanged()
            }
        }
    }
}