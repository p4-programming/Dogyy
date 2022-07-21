package com.bnb.doggydoo.dogsitting.adapter

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.SingleItemViewallBinding
import com.bnb.doggydoo.dogsitting.datasource.model.ViewAllDogsitPetList
import com.bnb.doggydoo.dogsitting.ui.DogSittingDetailActivity
import com.bnb.doggydoo.dogsitting.ui.DogSittingHerosDetailActivity
import com.bnb.doggydoo.utils.network.ApiConstant
import java.util.*
import kotlin.collections.ArrayList

class ViewAllDogsitAdapter(var context: Context, var viewType: String) :
    RecyclerView.Adapter<ViewAllDogsitAdapter.DogsittingViewHolder>(), Filterable {

    var petList: ArrayList<ViewAllDogsitPetList> = ArrayList()
    var petListFiltered: ArrayList<ViewAllDogsitPetList> = ArrayList()

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
        fun bind(detail: ViewAllDogsitPetList) {

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
                binding.tvNameAge.text = detail.uname
                binding.dogImage.loadImageFromString(
                    context,
                    ApiConstant.PROFILE_IMAGE_BASE_URL + detail.profile_pic
                )

                binding.mainLayout.setOnClickListener {
                    context.startActivity(
                        Intent(context, DogSittingHerosDetailActivity::class.java)
                            .putExtra("dogsitHeroId", detail.id))
                }


            }else{
                binding.tvNameAge.text = detail.pet_name + ","+ " "+ detail.pet_age
                binding.tvBreed.text = detail.breed
                binding.dogImage.loadImageFromString(
                    context,
                    ApiConstant.PET_IMAGE_BASE_URL + detail.pet_image
                )

                binding.mainLayout.setOnClickListener {
                    context.startActivity(
                        Intent(context, DogSittingDetailActivity::class.java)
                            .putExtra("petid", detail.pet_id))
                }
            }


        }
    }

    fun addData(list: List<ViewAllDogsitPetList>) {
        petList = list as ArrayList<ViewAllDogsitPetList>
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
                    val resultList = ArrayList<ViewAllDogsitPetList>()
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
                petListFiltered = results?.values as ArrayList<ViewAllDogsitPetList>
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
                    val filteredList = ArrayList<ViewAllDogsitPetList>()
                    petList.filter {
                            (it.breed.contains(constraint!!)) or (it.pet_name.contains(constraint))
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
                    results.values as ArrayList<ViewAllDogsitPetList>
                notifyDataSetChanged()
            }
        }
    }*/
}