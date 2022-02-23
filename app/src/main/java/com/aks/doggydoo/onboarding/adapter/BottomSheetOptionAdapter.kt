package com.aks.doggydoo.onboarding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleOptionBinding

private const val TAG = "bsOptionTag"

class BottomSheetOptionAdapter(
    var context: Context,
    var options: List<String>,
    private val getSelectedOption: (selectedOption: String) -> Unit
) : RecyclerView.Adapter<BottomSheetOptionAdapter.BottomSheetOptionViewHolder>() {

    //using it to update the views
    private var selectedItem = 0
    private var type = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetOptionViewHolder {
        return BottomSheetOptionViewHolder(
            SingleOptionBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ), getSelectedOption
        )
    }

    fun callSelectedBreed(breedType: String) {
        selectedItem = options.indexOf(breedType)
    }

    fun callOnlyForSelectedAgeType(type: String) {
        this.type = type
        if (type == "Months") {
            selectedItem = 1
        } else if (type == "Yrs") {
            selectedItem = 0
        }
    }

    fun callOnlyForSelectedWeightType(type: String) {
        this.type = type
        if (type == "Lbs") {
            selectedItem = 1
        } else if (type == "Kgs") {
            selectedItem = 0
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: BottomSheetOptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    inner class BottomSheetOptionViewHolder(
        var binding: SingleOptionBinding,
        getSelectedOption: (selectedOption: String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.parent.setOnClickListener {
                selectedItem = absoluteAdapterPosition
                notifyDataSetChanged()
                getSelectedOption(binding.textView.text.toString())
            }
        }

        fun bind(option: String) {
            binding.apply {
                textView.text = option

            }
            if (selectedItem == absoluteAdapterPosition) {
                binding.tick.show()
                binding.textView.setTextColor(context.resources.getColor(R.color.on_boarding_blue))
                getSelectedOption(binding.textView.text.toString())
            } else {
                binding.tick.hide()
                binding.textView.setTextColor(context.resources.getColor(R.color.black))
            }
        }
    }
}