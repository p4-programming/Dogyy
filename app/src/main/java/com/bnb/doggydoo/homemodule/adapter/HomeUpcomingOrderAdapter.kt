package com.bnb.doggydoo.homemodule.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.databinding.SingleUpcomingOrderBinding

private const val TAG = "upcomingTag"

class HomeUpcomingOrderAdapter(
    var context: Context,
    private val goToOrderDescriptionClass: (view: View) -> Unit
) :
    RecyclerView.Adapter<HomeUpcomingOrderAdapter.HomeUpcomingOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeUpcomingOrderViewHolder {
        val binding =
            SingleUpcomingOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeUpcomingOrderViewHolder(binding,goToOrderDescriptionClass)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: HomeUpcomingOrderViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.binding.backgroundImage.backgroundTintList =
                context.resources.getColorStateList(R.color.hint_color)
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class HomeUpcomingOrderViewHolder(var binding: SingleUpcomingOrderBinding,var goToOrderDescriptionClass:(view:View)->Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.parentLayout.setOnClickListener {
                goToOrderDescriptionClass(binding.orderImage)
            }
        }
    }

}