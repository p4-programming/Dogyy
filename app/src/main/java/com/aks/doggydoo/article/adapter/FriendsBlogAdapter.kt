package com.aks.doggydoo.article.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.article.datasource.model.Articledetail
import com.aks.doggydoo.article.ui.ArticleDetail
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleArticleBinding
import com.aks.doggydoo.utils.network.ApiConstant
import java.time.LocalDate
import java.time.Period

class FriendsBlogAdapter (var context: Context) :
    ListAdapter<Articledetail, FriendsBlogAdapter.ArticleViewHolder>(DiffCallback()) {
    var yearS: Int = 0
    var monthS: Int = 0
    var dayS: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            SingleArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(var binding: SingleArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(detail: Articledetail) {
            binding.tvTitle.text = detail.caption
            binding.tvDescription.text = detail.article
            binding.tvLikeCount.text = detail.countlike
            binding.tvCommentCount.text = detail.commentcount
            binding.articleProfile.loadImageFromString(
                context,
                ApiConstant.PROFILE_IMAGE_BASE_URL + detail.profile_pic
            )
            binding.name.text = detail.username
            binding.tvPostTime.text = detail.post_date


            binding.mainLayout.setOnClickListener {
                context.startActivity(Intent(context, ArticleDetail::class.java)
                    .putExtra("article_id", detail.id)
                    .putExtra("type","")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<Articledetail>() {
            override fun areItemsTheSame(oldItem: Articledetail, newItem: Articledetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Articledetail,
                newItem: Articledetail
            ): Boolean = oldItem.equals(newItem)
        }
    }
}