package com.aks.doggydoo.homemodule.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemUserBinding
import com.aks.doggydoo.homemodule.datasource.model.home.AllUserList
import com.aks.doggydoo.myprofile.ui.UserProfileActivity
import com.aks.doggydoo.utils.network.ApiConstant

class AllUserAdapter (var context: Context
) :
    RecyclerView.Adapter<AllUserAdapter.AllUserViewHolder>(), Filterable {
    var userList: ArrayList<AllUserList> = ArrayList()
    var userListFiltered: ArrayList<AllUserList> = ArrayList()
    var address: String =""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUserViewHolder {
        val binding =
            SingleItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllUserViewHolder, position: Int) {
        holder.bindData(userListFiltered[position])
    }
    fun addData(list: List<AllUserList>) {
        userList = list as ArrayList<AllUserList>
        userListFiltered = userList
        notifyDataSetChanged()
    }

    inner class AllUserViewHolder(var binding: SingleItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: AllUserList) {
            binding.apply {
                if (data.uname != null){
                    name.text = data.uname.trim()
                }else{
                    name.text = "-"
                }
                tvMobileNo.text = data.mobile

                ProfilePicture.loadImageFromString(
                    context,
                    ApiConstant.PROFILE_IMAGE_BASE_URL + data.profile_pic
                )

               /* try {
                    val geocoder: Geocoder
                    val addresses: List<Address>
                    geocoder = Geocoder(context, Locale.getDefault())

                    addresses = geocoder.getFromLocation(
                        data.lattitute.toDouble(),
                        data.longitute.toDouble(),
                        1
                    )
                    address = addresses[0].getAddressLine(0)

                }catch (e:Exception){

                }
                tvAddress.text = address*/

            }

            binding.mainLayout.setOnClickListener {
                context.startActivity(
                    Intent(context, UserProfileActivity::class.java)
                        .putExtra("viewuserid", data.id),
                )
            }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                userListFiltered = if (charSearch.isEmpty()) {
                    userList
                } else {
                    val resultList = ArrayList<AllUserList>()
                    for (row in userList) {
                        if (row.uname.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = userListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userListFiltered = results?.values as ArrayList<AllUserList>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = userListFiltered.size


}