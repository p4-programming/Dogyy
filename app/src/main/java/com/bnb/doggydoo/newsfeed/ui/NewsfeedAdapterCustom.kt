package com.bnb.doggydoo.newsfeed.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.SingleItemNewsfeedBinding
import com.bnb.doggydoo.databinding.SingleNewsfeedCustomBinding
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.bnb.doggydoo.myprofile.ui.UserProfileActivity
import com.bnb.doggydoo.newsfeed.adapter.NewsFeedDataAdapter
import com.bnb.doggydoo.newsfeed.datasource.model.NewsFeedDetail
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.network.ApiConstant

class NewsfeedAdapterCustom(private var context: Context, private var callingFrom: String,
                            private var callRequestedUserInfo: (userId: String, type: String) -> Unit
) :
    RecyclerView.Adapter<NewsfeedAdapterCustom.NewsFeedUploadedDataViewHolder>() {
    var petList: ArrayList<NewsFeedDetail> = ArrayList()
    var petListFiltered: ArrayList<NewsFeedDetail> = ArrayList()
    private var content: String = ""
    private var caption: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsFeedUploadedDataViewHolder {
        val binding =
            SingleNewsfeedCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsFeedUploadedDataViewHolder(binding)

    }

//    fun onBindViewHolder(holder: NewsFeedUploadedDataViewHolder, position: Int) {
//        holder.bind(petListFiltered[position])
//    }

    inner class NewsFeedUploadedDataViewHolder(var binding: SingleNewsfeedCustomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: NewsFeedDetail) {
            binding.ivUser.loadImageFromString(
                context,
                ApiConstant.PROFILE_IMAGE_BASE_URL + detail.user_pic
            )
            binding.tvUserName.text = detail.user_name
            binding.tvTime.text = detail.post_date

            binding.ivUser.setOnClickListener {
                context.startActivity(
                    Intent(context, UserProfileActivity::class.java)

                        .putExtra("viewuserid", detail.user_id)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }

            binding.apply {
                if (detail.news_type == "media") {
                    if (detail.filetype == "image") {
                        imageLayout.show()
                        articleLayout.hide()
                        video.hide()
                        dogImage.loadImageFromString(
                            context,
                            ApiConstant.BLOG_IMAGE_BASE_URL + detail.file
                        )
                        dogImage.setOnClickListener {
                            context.startActivity(
                                Intent(context, ArticleDetailsActivity::class.java)
                                    .putExtra("type", "image")
                                    .putExtra("url", ApiConstant.BLOG_IMAGE_BASE_URL + detail.file)
                                    .putExtra("caption", "")
                                    .putExtra("description", "")
                                    .putExtra("likeCount", detail.countlike)
                                    .putExtra("isLiked", detail.like)
                                    .putExtra("commentCount", detail.commentcount)
                                    .putExtra("newsfeedId", detail.id)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }

                        if (detail.like == "1") {
                            ivDoLike1.setColorFilter(ContextCompat.getColor(context, R.color.red))
                        } else {
                            ivDoLike1.setColorFilter(ContextCompat.getColor(context, R.color.black))
                        }

                        tvCaption.text = detail.caption
                        tvUploadedBy.text = detail.user_name
                        tvlike1.text = detail.countlike
                        tvComment1.text = detail.commentcount

                    } else {
                        imageLayout.show()
                        articleLayout.hide()
                        video.show()
                        dogImage.hide()

                        video.setOnClickListener {
                            context.startActivity(
                                Intent(context, ArticleDetailsActivity::class.java)
                                    .putExtra("type", "video")
                                    .putExtra("url", ApiConstant.BLOG_IMAGE_BASE_URL + detail.file)
                                    .putExtra("caption", "")
                                    .putExtra("description", "")
                                    .putExtra("likeCount", detail.countlike)
                                    .putExtra("isLiked", detail.like)
                                    .putExtra("commentCount", detail.commentcount)
                                    .putExtra("newsfeedId", detail.id)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                        }

                        if (detail.like == "1") {
                            ivDoLike1.setColorFilter(ContextCompat.getColor(context, R.color.red))
                        } else {
                            ivDoLike1.setColorFilter(ContextCompat.getColor(context, R.color.black))
                        }

                        tvCaption.text = detail.caption
                        tvUploadedBy.text = detail.user_name
                        tvlike1.text = detail.countlike
                        tvComment1.text = detail.commentcount
                    }

                    rlDoLike1.setOnClickListener {
                        if (detail.like == "1") {
                            callRequestedUserInfo(detail.id, "unlike")
                        } else {
                            callRequestedUserInfo(detail.id, "like")
                        }

                    }

                    rlDoComment1.setOnClickListener {
                        context.startActivity(
                            Intent(context, CommentActivity::class.java)
                                .putExtra("newsFeedId", detail.id)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }

                } else {
                    imageLayout.hide()
                    articleLayout.show()

                    content.text = detail.article
                    title.text = detail.caption
                    tvLike.text = detail.countlike
                    tvComment.text = detail.commentcount

                    if (detail.like == "1") {
                        ivDoLike.setColorFilter(ContextCompat.getColor(context, R.color.red))
                    } else {
                        ivDoLike.setColorFilter(ContextCompat.getColor(context, R.color.black))
                    }

                    articleLayout.setOnClickListener {
                        context.startActivity(
                            Intent(context, ArticleDetailsActivity::class.java)
                                .putExtra("type", "article")
                                .putExtra("url", ApiConstant.BLOG_IMAGE_BASE_URL + detail.file)
                                .putExtra("caption", detail.caption)
                                .putExtra("description", detail.article)
                                .putExtra("likeCount", detail.countlike)
                                .putExtra("commentCount", detail.commentcount)
                                .putExtra("isLiked", detail.like)
                                .putExtra("newsfeedId", detail.id)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }


                    rlDoLike.setOnClickListener {
                        if (detail.like == "1") {
                            callRequestedUserInfo(detail.id, "unlike")
                        } else {
                            callRequestedUserInfo(detail.id, "like")
                        }

                    }

                    rlDoComment.setOnClickListener {
                        context.startActivity(
                            Intent(context, CommentActivity::class.java)
                                .putExtra("newsFeedId", detail.id)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }
                }


                ivMenu.setOnClickListener {
                    blockDialog(
                        detail.user_id,
                        detail.user_name,
                        detail.user_pic,
                        detail.userUid,
                        detail
                    )
                }
            }

//            for(i in petList){
//               var position = petList.indexOf(i)
//                if(position % 2 ==0)
//                    binding.mainLayout.setBackgroundResource(R.drawable.accept_bg)
//                else
//                    binding.mainLayout.setBackgroundResource(R.drawable.blue_bg)
//            }

            //binding.mainLayout.setBackgroundResource(R.drawable.accept_bg)
        }
    }

    fun addData(list: List<NewsFeedDetail>) {
        petList = list as ArrayList<NewsFeedDetail>
        petListFiltered = petList

        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = petListFiltered.size


    private fun blockDialog(
        userId: String,
        userName: String,
        userPic: String?,
        userUid: String,
        detail: NewsFeedDetail
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_block)

        val tvBlock = dialog.findViewById<View>(R.id.tvBlock) as TextView
        val tvMessage = dialog.findViewById<View>(R.id.tvMessage) as TextView
        val tvShare = dialog.findViewById<View>(R.id.tvShare) as TextView
        val ivCancel = dialog.findViewById<View>(R.id.ivCancel) as ImageView
        val delete = dialog.findViewById<View>(R.id.tvDelete) as TextView

        if (callingFrom == "My Post"){
            delete.show()
        }else{
            delete.hide()
        }
        delete.setOnClickListener {
            if (MyApp.getSharedPref().userId == detail.user_id) {
                callRequestedUserInfo(detail.id, "delete")
                dialog.dismiss()
            } else {
                Toast.makeText(context, "You can not delete this post.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        tvBlock.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Are you sure want to block this person?")
            builder.setMessage("This person will be permanently blocked and you won’t be able to see any of his/her posts in the newsfeed module.")

            builder.setPositiveButton("Yes") { dialog1, which ->
                if (MyApp.getSharedPref().userId == userId) {
                    Toast.makeText(context, "You can not block yourself.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    callRequestedUserInfo(userId, "block")
                    dialog.dismiss()
                    dialog1.dismiss()
                }
            }

            builder.setNegativeButton("No") { dialog1, which ->
                dialog.dismiss()
                dialog1.dismiss()
            }

            builder.show()

        }

        tvMessage.setOnClickListener {
            if (MyApp.getSharedPref().userId == userId) {
                Toast.makeText(context, "Can't message yourself.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                if (userUid.isBlank()) {
                    Toast.makeText(context, "User is not registered.", Toast.LENGTH_SHORT).show()
                } else {
                    context.startActivity(
                        Intent(context, ChatActivity::class.java)
                            .putExtra("name", userName)
                            .putExtra("uid", userUid)
                            .putExtra("userImage", userPic)
                            .putExtra("clicked_user_id",userId)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )

                }
                dialog.dismiss()
            }

        }

        tvShare.setOnClickListener {
            //Toast.makeText(context, "Type==" + detail.filetype, Toast.LENGTH_SHORT).show()
            if (detail.news_type == "media") {
                if (detail.filetype == "image") {
                    content = (ApiConstant.BLOG_IMAGE_BASE_URL + detail.file).toString()
                    caption = detail.caption.toString()

                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "$caption:\n $content")
                    context.startActivity(Intent.createChooser(intent, "Share with:"))

                    dialog.dismiss()

                } else {
                    content = (ApiConstant.BLOG_IMAGE_BASE_URL + detail.file).toString()
                    caption = detail.caption.toString()

                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "$caption:\n $content")
                    context.startActivity(Intent.createChooser(intent, "Share with:"))

                    dialog.dismiss()
                }

            } else {
                content = detail.article.toString()
                caption = detail.caption.toString()

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "$caption:\n $content")
                context.startActivity(Intent.createChooser(intent, "Share with:"))

                dialog.dismiss()
            }

        }

        ivCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

//    override fun onBindViewHolder(
//        holder: NewsFeedDataAdapter.NewsFeedUploadedDataViewHolder,
//        position: Int
//    ) {
//        holder.bind(petListFiltered[position])
//    }

    override fun onBindViewHolder(holder: NewsFeedUploadedDataViewHolder, position: Int) {
        if(position%2 == 0)
        holder.binding.mainLayout.setBackgroundResource(R.drawable.pink_bg)
        else
            holder.binding.mainLayout.setBackgroundResource(R.drawable.accept_bg)
        holder.bind(petListFiltered[position])
    }
}