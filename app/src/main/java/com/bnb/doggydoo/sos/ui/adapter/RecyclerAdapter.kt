package com.bnb.doggydoo.sos.ui.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.mydog.ui.DDPActivity
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.network.ApiConstant
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class RecyclerAdapter(private val mContext: Context, private val DataList: ArrayList<getDistressPinByUserID.Datum>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    private var petImage: String = ""

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.mythreadcard, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentitem = DataList[position]
        Log.d("Deepak","Petid : ${currentitem.id}")
        Log.d("Deepak","Userid : ${MyApp.getSharedPref().userId}")
        viewHolder.discription.text = currentitem.petDescription
        viewHolder.cdate.text = currentitem.createdDate

        if (currentitem.petImage.isNotEmpty()) {
            petImage = ApiConstant.PET_IMAGE_BASE_URL + currentitem.petImage
            Log.d("Deepak","PetImageIf : $petImage")
            Glide.with(mContext)
                .load(petImage)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform()
                )
                .into(viewHolder.dogimgs)
        } else {
            Log.d("Deepak","PetImageElse")
            Glide.with(mContext).load(R.drawable.dummy_pet).into(viewHolder.dogimgs)
        }

        viewHolder.cardll.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, DDPActivity::class.java)
                    .putExtra("petId", currentitem.id)
            )
        }

        if (currentitem.status.equals("0")){
            viewHolder.dogimgs.setBackgroundColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val discription: TextView = view.findViewById(R.id.discription)
        val cdate: TextView = view.findViewById(R.id.cdate)
        val dogimgs: ImageView  = view.findViewById(R.id.dogimgs)
        var cardll :LinearLayout=view.findViewById(R.id.cardll)
    }
}

