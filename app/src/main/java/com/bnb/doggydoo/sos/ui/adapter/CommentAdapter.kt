package com.bnb.doggydoo.sos.ui.adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.mydog.datasource.model.getDistressPinByUserID
import com.bnb.doggydoo.myprofile.ui.MyProfileActivity
import com.bnb.doggydoo.newsfeed.datasource.model.NewsFeedCommentDetail
import com.bnb.doggydoo.sos.ui.model.CommentModels
import com.bnb.doggydoo.utils.network.ApiConstant

class CommentAdapter(private val mContext: Context, private val DataList: ArrayList<CommentModels>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.single_user_comment, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = DataList[position]
        holder.name.text = currentitem.name
        holder.mobile.text = currentitem.comment
        holder.cdate.text = currentitem.time
        holder.usepic.loadImageFromString(mContext, currentitem.userPic)

        holder.usepic.setOnClickListener {
            mContext.startActivity(Intent(mContext, MyProfileActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name1)
        val mobile: TextView = itemView.findViewById(R.id.tvMobileNo1)
        val cdate: TextView = itemView.findViewById(R.id.cdate1)
        val usepic: ImageView = itemView.findViewById(R.id.ProfilePicture1)
    }
}
