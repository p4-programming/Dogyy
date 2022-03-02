package com.aks.doggydoo.adoption.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.adoptdogdetails.ui.AdoptDogDetailActivity
import com.aks.doggydoo.adoption.datasource.model.AdoptionListAllData
import com.aks.doggydoo.adoption.datasource.model.Pet
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleAdoptionViewAllBinding
import com.aks.doggydoo.utils.network.ApiConstant
import java.util.*
import kotlin.collections.ArrayList

class ViewAllSheltersDogAdapter(var context: Context) :
    RecyclerView.Adapter<ViewAllSheltersDogAdapter.ViewAllViewHolder>(), Filterable {

    var petList: ArrayList<Pet> = ArrayList()
    var petListFiltered: ArrayList<Pet> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllViewHolder {
        Log.d("mhsdafdfa", "onCreateViewHolder: ")
        return ViewAllViewHolder(
            SingleAdoptionViewAllBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewAllViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#F2EEE1"))
        } else {
            holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#FFE9EF"))
        }
        holder.bindAdoptionData(petListFiltered[position])
    }

    inner class ViewAllViewHolder(var binding: SingleAdoptionViewAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindAdoptionData(data: Pet) {
            binding.apply {
                adoptName.text =
                    "${data.pet_name}, ${data.pet_age} Years , ${data.pet_age_month} month"
                adoptBreed.text = data.breed

                /* try {
                     val addresses: List<Address>
                     val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

                     addresses = geocoder.getFromLocation(
                         data.lattitue.toDouble(),
                         data.longitute.toDouble(),
                         1
                     )

                     val address = addresses[0].locality
                     if (address.isNotEmpty()) {
                         tvLocation.show()
                         tvLocation.text = address
                     } else {
                         tvLocation.hide()
                     }


                 } catch (e: Exception) {

                 }*/

                if (data.pet_image == "user.png") {
                    dogImage.setImageResource(R.drawable.dummy_pet)
                } else
                    dogImage.loadImageFromString(
                        context,
                        ApiConstant.PET_IMAGE_BASE_URL + data.pet_image
                    )
            }

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, AdoptDogDetailActivity::class.java)
                        .putExtra("adoptId", data.pet_id),
                )
            }
        }
    }

    fun addData(list: List<Pet>) {
        petList = list as ArrayList<Pet>
        petListFiltered = petList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                petListFiltered = if (charSearch.isEmpty()) {
                    petList
                } else {
                    val resultList = ArrayList<Pet>()
                    for (row in petList) {
                        if (row.pet_name.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = petListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                petListFiltered = results?.values as ArrayList<Pet>
                notifyDataSetChanged()
            }
        }
    }


    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) petListFiltered = petList else {
                    val filteredList = ArrayList<AdoptionListAllData>()
                    petList
                        .filter {
                            (it.breed.contains(constraint!!)) or
                                    (it.name.contains(constraint))

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
                    results.values as ArrayList<AdoptionListAllData>
                notifyDataSetChanged()
            }
        }
    }*/

    override fun getItemCount(): Int = petListFiltered.size
}