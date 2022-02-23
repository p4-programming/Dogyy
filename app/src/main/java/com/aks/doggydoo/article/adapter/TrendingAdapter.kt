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


class TrendingAdapter(var context: Context) :
    ListAdapter<Articledetail, TrendingAdapter.ArticleViewHolder>(DiffCallback()) {
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
            try {
                val postdate = detail.post_date
                val value = postdate.split(" ").toTypedArray()
                val date: String = value[0]
                val dateS = date.split("-").toTypedArray()
                yearS = dateS[0].toInt()
                monthS = dateS[1].toInt()
                dayS = dateS[2].toInt()

                val today = LocalDate.now()
                val birthday: LocalDate = LocalDate.of(yearS, monthS, dayS)
                val postedDuration = Period.between(birthday, today)
                val text = postedDuration.toString().replace("P", "")
                if (text=="0D"){
                    binding.tvPostTime.text = "Today"
                }else{
                    binding.tvPostTime.text = text
                }

            } catch (e: Exception) {
            }


            binding.mainLayout.setOnClickListener {
                context.startActivity(Intent(context, ArticleDetail::class.java)
                    .putExtra("article_id", detail.id)
                    .putExtra("type","trending")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }

        }
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