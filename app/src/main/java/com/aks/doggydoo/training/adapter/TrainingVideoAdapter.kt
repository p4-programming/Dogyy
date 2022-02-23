package com.aks.doggydoo.training.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.loadImageFromString
import com.aks.doggydoo.databinding.TrainingVideoItemBinding
import com.aks.doggydoo.training.datasource.model.TrainingListDetail
import com.aks.doggydoo.training.ui.FullScreenVideoActivity
import com.aks.doggydoo.training.ui.YoutubeActivity
import java.net.MalformedURLException
import java.net.URL


class TrainingVideoAdapter(var context: Context) :
    ListAdapter<TrainingListDetail, TrainingVideoAdapter.TrainingVideoViewHolder>(DiffCallback()) {
    var videoId: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingVideoViewHolder {
        val binding = TrainingVideoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrainingVideoViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: TrainingVideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TrainingVideoViewHolder(var binding: TrainingVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: TrainingListDetail) {
            binding.tvTitle.text = detail.caption
            binding.tvDate.text = detail.createdate
           // Toast.makeText(context,detail.filetype,Toast.LENGTH_SHORT).show()

            val videoFile = "http://www.youtube.com/watch?v="+detail.file
            videoId = extractYoutubeId(videoFile)
            val img_url = "http://img.youtube.com/vi/$videoId/0.jpg"
            binding.ivThumbnail.loadImageFromString(context, img_url)


            binding.playVideoLayout.setOnClickListener {
                if (detail.filetype == "Youtube") {
                    context.startActivity(
                        Intent(context, YoutubeActivity::class.java)
                            .putExtra("videoUrl", detail.file)
                            .putExtra("trainId", detail.id)
                            .putExtra("title", detail.caption)
                    )
                } else {
                    context.startActivity(
                        Intent(context, FullScreenVideoActivity::class.java)
                            .putExtra("videoUrl", detail.file)
                            .putExtra("trainId", detail.id)
                            .putExtra("title", detail.caption)
                    )
                }

            }

        }
    }

    @Throws(MalformedURLException::class)
    fun extractYoutubeId(url: String?): String? {
        val query: String = URL(url).getQuery()
        val param = query.split("&").toTypedArray()
        var id: String? = null
        for (row in param) {
            val param1 = row.split("=").toTypedArray()
            if (param1[0] == "v") {
                id = param1[1]
            }
        }
        return id
    }

    companion object {
        class DiffCallback : DiffUtil.ItemCallback<TrainingListDetail>() {
            override fun areItemsTheSame(oldItem: TrainingListDetail, newItem: TrainingListDetail) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: TrainingListDetail,
                newItem: TrainingListDetail
            ): Boolean = oldItem.equals(newItem)
        }
    }
}