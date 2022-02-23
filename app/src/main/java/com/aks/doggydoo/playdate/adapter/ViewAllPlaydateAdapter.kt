package com.aks.doggydoo.playdate.adapter

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleItemViewallBinding
import com.aks.doggydoo.playdate.datasource.model.homepage.LastPlayDates
import com.aks.doggydoo.playdate.ui.PlayDateDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant
import java.util.*
import kotlin.collections.ArrayList

class ViewAllPlaydateAdapter (var context: Context) :
    RecyclerView.Adapter<ViewAllPlaydateAdapter.ViewAllPlayDateViewHolder>(), Filterable {

    var petList: ArrayList<LastPlayDates> = ArrayList()
    var petListFiltered: ArrayList<LastPlayDates> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllPlayDateViewHolder {
        val binding =
            SingleItemViewallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewAllPlayDateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewAllPlayDateViewHolder, position: Int) {
        holder.bind(petListFiltered[position])
    }

    fun addData(list: List<LastPlayDates>) {
        petList = list as ArrayList<LastPlayDates>
        petListFiltered = petList
        notifyDataSetChanged()
    }

    inner class ViewAllPlayDateViewHolder(var binding: SingleItemViewallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: LastPlayDates) {
            binding.tvNameAge.text = "${detail.pet_name}, ${detail.pet_age} Years"
            binding.tvBreed.text = detail.breed

            try {
                val addresses: List<Address>
                val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

                addresses = geocoder.getFromLocation(
                    detail.lattitue.toDouble(),
                    detail.longitute.toDouble(),
                    1
                )

                val address = addresses[0].locality
                if (address.isNotEmpty()){
                    binding.tvLocation.show()
                    binding.tvLocation.text = address
                }else{
                    binding.tvLocation.hide()
                }


            }catch (e:Exception){

            }



            binding.dogImage.loadImageFromString(
                context,
                ApiConstant.PET_IMAGE_BASE_URL + detail.pet_image
            )


            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, PlayDateDetailActivity::class.java).putExtra(
                        "pet_id",
                        detail.id
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    override fun getItemCount(): Int = petListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                petListFiltered = if (charSearch.isEmpty()) {
                    petList
                } else {
                    val resultList = ArrayList<LastPlayDates>()
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
                petListFiltered = results?.values as ArrayList<LastPlayDates>
                notifyDataSetChanged()
            }
        }
    }
/*
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) petListFiltered = petList else {
                    val filteredList = ArrayList<LastPlayDates>()
                    petList
                        .filter {
                            (it.breed.contains(constraint!!)) or
                                    (it.pet_name.contains(constraint))

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
                    results.values as ArrayList<LastPlayDates>
                notifyDataSetChanged()
            }
        }
    }*/
}