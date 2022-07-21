package com.bnb.doggydoo.homemodule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bnb.doggydoo.R
import com.bnb.doggydoo.fostering.ui.FosteringActivity
import com.bnb.doggydoo.adoption.ui.AdoptionIntro
import com.bnb.doggydoo.article.ui.ArticleIntro
import com.bnb.doggydoo.callawet.ui.CallAWetIntro
import com.bnb.doggydoo.databinding.SingleHomeFeatureBinding
import com.bnb.doggydoo.dogsitting.ui.DogSittingActivity
import com.bnb.doggydoo.newsfeed.ui.NewsfeedActivity
import com.bnb.doggydoo.playdate.ui.PlayDateActivity
import com.bnb.doggydoo.sos.ui.SOSIntro
import com.bnb.doggydoo.training.ui.TrainingActivity

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
        holder.binding.lottie.setAnimation(bgDrawableIds.get(position))
       // holder.binding.image.setImageResource(bgDrawableIds.get(position));

        when {
            name[position] == "Fostering" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.fostering)
            }
            name[position] == "Adoption" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.adoption)

            }
            name[position] == "Playdate" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.playDate)

            }
            name[position] == "NewsFeed" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.white)

            }
            name[position] == "DogSitting" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.dogSitting)

            }
            name[position] == "Find a vet" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.callAVet)

            }
            name[position] == "Training" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.training)

            }
            name[position] == "Article" -> {
                holder.binding.mainLayout.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.articles)

            }

        }
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class HomeFeatureViewHolder(var binding: SingleHomeFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lottie.setOnClickListener {
                when (binding.tvTitle.text) {
                    "Adoption" -> {
                        context.startActivity(Intent(context, AdoptionIntro::class.java))
                    }
                    "Playdate" -> {
                        context.startActivity(Intent(context, PlayDateActivity::class.java))
                    }
                    "NewsFeed" -> {
                        context.startActivity(Intent(context, NewsfeedActivity::class.java))
                        // context.startActivity(Intent(context, NewsFeedDashboardActivity::class.java))
                    }
                    "Fostering" -> {
                        context.startActivity(Intent(context, FosteringActivity::class.java))
                        binding.mainLayout.setBackgroundColor(R.string.add_card)
                    }
                    "DogSitting" -> {
                        context.startActivity(Intent(context, DogSittingActivity::class.java))
                    }
                    "Find a vet" -> {
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