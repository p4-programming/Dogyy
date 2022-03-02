package com.aks.doggydoo.shelter.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aks.doggydoo.R
import com.aks.doggydoo.adoptdogdetails.ui.AdoptDogDetailActivity
import com.aks.doggydoo.adoption.adapter.AdoptionAdapter
import com.aks.doggydoo.adoption.adapter.AdoptionShelterAdapter
import com.aks.doggydoo.adoption.adapter.ShelterAdapter
import com.aks.doggydoo.adoption.ui.AdoptionViewAll
import com.aks.doggydoo.adoption.viewmodel.AdoptionModel
import com.aks.doggydoo.chatMessage.titelArray
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityDogShelterBinding
import com.aks.doggydoo.shelter.adapter.HelpingHandAdapter
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogShelterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDogShelterBinding

    private lateinit var adapter: AdoptionShelterAdapter
    private val adoptionModel: AdoptionModel by viewModels()
    private var shelterId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogShelterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callGetShelterListApi()
    }

    private fun getInit() {
        val title: String = intent.getStringExtra("title").toString()
        shelterId = intent.getStringExtra("shelter_id").toString()
        binding.tvTitle.text = title
        adapter = AdoptionShelterAdapter(this@DogShelterActivity) { it, id ->
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@DogShelterActivity,
                it,
                it.transitionName
            )

            startActivity(
                Intent(this@DogShelterActivity, AdoptDogDetailActivity::class.java)
                    .putExtra("adoptId", id),
                option.toBundle()
            )
        }
        binding.homeRv.adapter = adapter

        //   binding.helpingHandRv.adapter = HelpingHandAdapter()
        binding.backButton.setOnClickListener { finish() }

        binding.searchview.setOnClickListener {
            binding.searchview.onActionViewExpanded()
            binding.searchText.hide()

        }
        binding.searchview.setOnSearchClickListener {
            binding.searchText.hide()
        }
        binding.searchview.setOnCloseListener {
            binding.searchview.onActionViewCollapsed()
            binding.searchText.show()
            true
        }

        binding.tvViewAllDogs.setOnClickListener {
            startActivity(
                Intent(this, AdoptionViewAll::class.java)
                    .putExtra("shelter_id", shelterId)
                    .putExtra("from", "Shelter").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun callGetShelterListApi() {
        adoptionModel.getShelterDetail(shelterId).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data!!.responseCode == ("0")) {
                        it.data.responseMessage.snack(
                            Color.RED,
                            binding.parent
                        )
                        return@Observer
                    }
                    adapter.submitList(it.data.shelterlist[0].Pet_List)
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                }
            }
        })
    }
}