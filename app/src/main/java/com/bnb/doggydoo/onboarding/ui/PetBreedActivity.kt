package com.bnb.doggydoo.onboarding.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityPetbreedBinding
import com.bnb.doggydoo.newsfeed.util.RecyclerTouchListener
import com.bnb.doggydoo.newsfeed.util.RecyclerTouchListener.ClickListener
import com.bnb.doggydoo.onboarding.adapter.BreedAdapter
import com.bnb.doggydoo.onboarding.datasource.model.pet.PetBreedDetail
import com.bnb.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PetBreedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetbreedBinding
    private lateinit var onBoardingViewModel: OnBoardingViewModel
    private lateinit var adapterBreed: BreedAdapter
    private var breed = ArrayList<String>()
    private var breedIdList = ArrayList<String>()
    private var valueBreed: String = ""
    private var valueBreedId: String = ""
    var petName: String = ""
    var petAgeYear: String =""
    var petAgeMonth: String =""
    private var breedList = ArrayList<PetBreedDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetbreedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        CommonMethod.makeTransparentStatusBar(window)
        getPetBreedData()

        binding.button.setOnClickListener{
            startActivity(
                Intent(this, PetGenderActivity::class.java)
                    .putExtra("petname", petName)
                    .putExtra("petageyear", petAgeYear)
                    .putExtra("petagemonth", petAgeMonth)
                    .putExtra("petbreedid", valueBreedId)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }


        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.etBreddSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               /* if (s != null) {
                    if (s.length > 3) {
                        val text: String = s.toString()
                    }
                }*/
                adapterBreed.filter.filter(s)
                adapterBreed.notifyDataSetChanged()
            }
        })

    }

    private fun getInit() {
        onBoardingViewModel = ViewModelProvider(this).get(OnBoardingViewModel::class.java)

        petName = intent.getStringExtra("petname").toString()
        petAgeYear = intent.getStringExtra("petageyear").toString()
        petAgeMonth = intent.getStringExtra("petagemonth").toString()

       // binding.gifImageView.loadImageAsGif(this, R.raw.on_board_dog_next)
        binding.llBreed.visibility = View.GONE
        binding.tvPetBreed.visibility = View.VISIBLE
        binding.i.visibility = View.VISIBLE
        binding.button.visibility = View.VISIBLE

        binding.tvPetBreed.setOnClickListener {
            binding.llBreed.visibility = View.VISIBLE
            binding.tvPetBreed.visibility = View.GONE
            binding.i.visibility = View.INVISIBLE
            binding.button.visibility = View.GONE
        }

        binding.tvConfirm.setOnClickListener {
            binding.llBreed.visibility = View.GONE
            binding.tvPetBreed.visibility = View.VISIBLE
            binding.i.visibility = View.VISIBLE
            binding.button.visibility = View.VISIBLE
        }

     /*   adapterSheltersDogViewALl = ViewAllSheltersDogAdapter(this@AdoptionViewAll)
        binding.rvAllItems.adapter = adapterSheltersDogViewALl
*/
        adapterBreed = BreedAdapter(this)
        binding.rvBreed.adapter = adapterBreed


        binding.rvBreed.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.rvBreed,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {


                        valueBreed = breed.get(position)
                        valueBreedId = breedIdList.get(position)


                        binding.tvPetBreed.text = valueBreed
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
    }

    private fun getPetBreedData() {
        onBoardingViewModel.getPetBreedData().observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
                    }
                    //set adapter
                    for (category in it.data.petbreeddetails) {
                        breed.add(category.category)
                        breedIdList.add(category.id)
                    }

                    renderList(it.data.petbreeddetails)
                   // adapterBreed.submitList(it.data.petbreeddetails)

                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.root)
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

    private fun renderList(data: List<PetBreedDetail>) {
        adapterBreed.addData(data)
    }

}