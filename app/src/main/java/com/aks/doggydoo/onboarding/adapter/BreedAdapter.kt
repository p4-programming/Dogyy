package com.aks.doggydoo.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.adoption.adapter.ViewAllAdapter
import com.aks.doggydoo.adoption.datasource.model.AdoptionListAllData
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleBreedItemBinding
import com.aks.doggydoo.onboarding.datasource.model.pet.PetBreedDetail

class BreedAdapter(var context: Context) : RecyclerView.Adapter<BreedAdapter.BreedViewHolder>(),
    Filterable {

    /*class BreedAdapter(var context: Context) :
        ListAdapter<PetBreedDetail, BreedAdapter.BreedViewHolder>(BreedAdapter.Companion.DiffCallback()), Filterable {*/
    var selectedPosition = -1

    var breedList: ArrayList<PetBreedDetail> = ArrayList()
    var breedListFiltered: ArrayList<PetBreedDetail> = ArrayList()


    inner class BreedViewHolder(var binding: SingleBreedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: PetBreedDetail) {
            binding.textView.text = detail.category.trim()
            if (selectedPosition == absoluteAdapterPosition) {
                binding.tick.show()
                binding.textView.setTextColor(context.resources.getColor(R.color.on_boarding_blue))
            } else {
                binding.tick.hide()
                binding.textView.setTextColor(context.resources.getColor(R.color.black))
            }

            binding.parent.setOnClickListener {
                selectedPosition = absoluteAdapterPosition;
                notifyDataSetChanged();
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val binding =
            SingleBreedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BreedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        //holder.bind(getItem(position)
        holder.bind(breedListFiltered[position])

    }


    fun addData(list: List<PetBreedDetail>) {
        breedList = list as ArrayList<PetBreedDetail>
        breedListFiltered = breedList
        notifyDataSetChanged()
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<PetBreedDetail>() {
            override fun areItemsTheSame(oldItem: PetBreedDetail, newItem: PetBreedDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PetBreedDetail,
                newItem: PetBreedDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                breedListFiltered = if (charSearch.isEmpty()) {
                    breedList
                } else {
                    val resultList = ArrayList<PetBreedDetail>()
                    for (row in breedList) {
                        if (row.category.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = breedListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                breedListFiltered = results?.values as ArrayList<PetBreedDetail>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = breedListFiltered.size


}