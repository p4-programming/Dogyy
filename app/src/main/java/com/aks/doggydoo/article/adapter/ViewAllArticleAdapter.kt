package com.aks.doggydoo.article.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.article.datasource.model.Articledetail
import com.aks.doggydoo.article.ui.ArticleDetail
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.SingleItemArticleviewallBinding
import com.aks.doggydoo.utils.network.ApiConstant
import java.time.LocalDate
import java.time.Period

class ViewAllArticleAdapter(var context: Context, viewType: String) :
    RecyclerView.Adapter<ViewAllArticleAdapter.ViewAllViewHolder>(), Filterable {
    var articleList: ArrayList<Articledetail> = ArrayList()
    var articleListFiltered: ArrayList<Articledetail> = ArrayList()
    var yearS: Int = 0
    var monthS: Int = 0
    var dayS: Int = 0
    var viewType = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllViewHolder {
        return ViewAllViewHolder(
            SingleItemArticleviewallBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewAllViewHolder, position: Int) {
        holder.bindAdoptionData(articleListFiltered[position])
    }

    inner class ViewAllViewHolder(var binding: SingleItemArticleviewallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindAdoptionData(detail: Articledetail) {
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
                if (text == "0D") {
                    binding.tvPostTime.text = "Today"
                } else {
                    binding.tvPostTime.text = text
                }

            } catch (e: Exception) {
            }

            binding.mainLayout.setOnClickListener {

                context.startActivity(
                    Intent(context, ArticleDetail::class.java)
                        .putExtra("article_id", detail.id)
                        .putExtra("type", viewType)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
    }

    fun addData(list: List<Articledetail>) {
        articleList = list as ArrayList<Articledetail>
        articleListFiltered = articleList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                articleListFiltered = if (charSearch.isEmpty()) {
                    articleList
                } else {
                    val resultList = ArrayList<Articledetail>()
                    for (row in articleList) {
                        if (row.caption.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = articleListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                articleListFiltered = results?.values as ArrayList<Articledetail>
                notifyDataSetChanged()
            }
        }
    }

/*
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) articleListFiltered = articleList else {
                    val filteredList = ArrayList<Articledetail>()
                    articleList
                        .filter {
                            (it.username.contains(constraint!!)) or
                                    (it.caption.contains(constraint))

                        }
                        .forEach { filteredList.add(it) }
                    articleListFiltered = filteredList

                }
                return FilterResults().apply { values = articleListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                articleListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Articledetail>
                notifyDataSetChanged()
            }
        }
    }*/

    override fun getItemCount(): Int = articleListFiltered.size
}