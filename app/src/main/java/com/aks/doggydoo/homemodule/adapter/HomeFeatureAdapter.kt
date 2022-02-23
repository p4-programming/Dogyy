package com.aks.doggydoo.homemodule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.fostering.ui.FosteringActivity
import com.aks.doggydoo.adoption.ui.AdoptionIntro
import com.aks.doggydoo.article.ui.ArticleIntro
import com.aks.doggydoo.callawet.ui.CallAWetIntro
import com.aks.doggydoo.databinding.SingleHomeFeatureBinding
import com.aks.doggydoo.dogsitting.ui.DogSittingActivity
import com.aks.doggydoo.newsfeed.ui.NewsFeedDashboardActivity
import com.aks.doggydoo.playdate.ui.PlayDateActivity
import com.aks.doggydoo.sos.ui.SOSIntro
import com.aks.doggydoo.training.ui.TrainingActivity

private const val TAG = "feturedTag"

class HomeFeatureAdapter(
    var context: Context,
    private val name: Array<String>,
    private val bgDrawableIds: IntArray
) : RecyclerView.Adapter<HomeFeatureAdapter.HomeFeatureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeatureViewHolder {
        return HomeFeatureViewHolder(
            SingleHomeFeatureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HomeFeatureViewHolder, position: Int) {
        holder.binding.tvTitle.text = name.get(position)
        holder.binding.image.setImageResource(bgDrawableIds.get(position));

     /*   when {
            name[position] == "Fostering" -> {
                holder.binding.llImage.setBackgroundColor(R.color.fostering)
            }
            name[position] == "Adoption" -> {
                holder.binding.llImage.setBackgroundColor(R.color.adoption)

            }
            name[position] == "Playdate" -> {
                holder.binding.llImage.setBackgroundColor(R.color.playDate)

            }
            name[position] == "NewsFeed" -> {
                holder.binding.llImage.setBackgroundColor(R.color.dogSitting)

            }
            name[position] == "SOS" -> {
                //holder.binding.mainLayout.setBackgroundColor(R.color.fostering)

            }
            name[position] == "Call a Vet" -> {
                holder.binding.llImage.setBackgroundColor(R.color.callAVet)

            }
            name[position] == "Training" -> {
                holder.binding.llImage.setBackgroundColor(R.color.training)

            }
            name[position] == "Article" -> {
                holder.binding.llImage.setBackgroundColor(R.color.articles)

            }

        }*/
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class HomeFeatureViewHolder(var binding: SingleHomeFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.image.setOnClickListener {
                when (binding.tvTitle.text) {
                    "Adoption" -> {
                        context.startActivity(Intent(context, AdoptionIntro::class.java))
                    }
                    "Playdate" -> {
                        context.startActivity(Intent(context, PlayDateActivity::class.java))
                    }
                    "NewsFeed" -> {
                        context.startActivity(
                            Intent(
                                context,
                                NewsFeedDashboardActivity::class.java
                            )
                        )
                    }
                    "Fostering" -> {
                        context.startActivity(Intent(context, FosteringActivity::class.java))
                        binding.mainLayout.setBackgroundColor(R.string.add_card)
                    }
                    "DogSitting" -> {
                        context.startActivity(Intent(context, DogSittingActivity::class.java))
                    }
                    "Call a Vet" -> {
                        context.startActivity(Intent(context, CallAWetIntro::class.java))
                    }
                    "Training" -> {
                        context.startActivity(Intent(context, TrainingActivity::class.java))
                    }
                    "Article" -> {
                        context.startActivity(Intent(context, ArticleIntro::class.java))
                    }
                    "SOS" -> {
                        context.startActivity(Intent(context, SOSIntro::class.java))
                    }
                }
            }
        }
    }
}