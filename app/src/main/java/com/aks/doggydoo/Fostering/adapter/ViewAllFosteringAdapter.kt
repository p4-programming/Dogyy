package com.aks.doggydoo.fostering.adapter

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
import com.aks.doggydoo.fostering.datasource.model.NearByFosterPetList
import com.aks.doggydoo.fostering.ui.FosteringDetailActivity
import com.aks.doggydoo.utils.network.ApiConstant
import java.util.*
import kotlin.collections.ArrayList

class ViewAllFosteringAdapter(var context: Context, var viewType: String) :
    RecyclerView.Adapter<ViewAllFosteringAdapter.DogsittingViewHolder>(), Filterable {

    var petList: ArrayList<NearByFosterPetList> = ArrayList()
    var petListFiltered: ArrayList<NearByFosterPetList> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsittingViewHolder {
        val binding =
            SingleItemViewallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogsittingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogsittingViewHolder, position: Int) {
        holder.bind(petListFiltered[position])
    }

    inner class DogsittingViewHolder(var binding: SingleItemViewallBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: NearByFosterPetList) {
            binding.tvNameAge.text = detail.name + ","+ " "+ detail.age
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



            if (viewType=="hero"){
                binding.dogImage.loadImageFromString(
                    context,
                    ApiConstant.PROFILE_IMAGE_BASE_URL + detail.pic
                )
            }else{
                binding.dogImage.loadImageFromString(
                    context,
                    ApiConstant.PET_IMAGE_BASE_URL + detail.pic
                )
            }

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, FosteringDetailActivity::class.java).putExtra(
                        "fostertId",
                        detail.foster_id
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    fun addData(list: List<NearByFosterPetList>) {
        petList = list as ArrayList<NearByFosterPetList>
        petListFiltered = petList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = petListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                petListFiltered = if (charSearch.isEmpty()) {
                    petList
                } else {
                    val resultList = ArrayList<NearByFosterPetList>()
                    for (row in petList) {
                        if (row.name.lowercase().contains(constraint.toString().lowercase())) {
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
                petListFiltered = results?.values as ArrayList<NearByFosterPetList>
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
                    val filteredList = ArrayList<NearByFosterPetList>()
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
                    results.values as ArrayList<NearByFosterPetList>
                notifyDataSetChanged()
            }
        }
    }*/
}