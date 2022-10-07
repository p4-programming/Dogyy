package com.bnb.doggydoo.sos.ui.adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.myprofile.datasource.model.profile.NewUploadData
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.newsfeed.datasource.model.NewsFeedCommentDetail
import com.bnb.doggydoo.newsfeed.ui.ArticleDetailsActivity
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.network.ApiConstant

class CommentAdapter(private val mContext: Context, private val DataList: ArrayList<NewsFeedCommentDetail>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.single_user_comment, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentitem = DataList[position]
        viewHolder.name.text = currentitem.username
        viewHolder.mobile.text = currentitem.comment
        viewHolder.cdate.text = currentitem.createon
        viewHolder.usepic.loadImageFromString(mContext, ApiConstant.PET_IMAGE_BASE_URL + currentitem.userphoto)

        viewHolder.usepic.setOnClickListener {
            if (currentitem.user_id != MyApp.getSharedPref().userId){
                mContext.startActivity(Intent(mContext, UserProfileActivity::class.java)
                    .putExtra("viewuserid", currentitem.user_id)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = itemView.findViewById(R.id.name1)
            val mobile: TextView = itemView.findViewById(R.id.tvMobileNo1)
            val cdate: TextView = itemView.findViewById(R.id.cdate1)
            val usepic: ImageView = itemView.findViewById(R.id.ProfilePicture1)


        fun bind(detail: NewUploadData) {
            val id = detail.id
        }
    }
}

