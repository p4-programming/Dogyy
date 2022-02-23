package com.aks.doggydoo.onboarding.bottomsheetfragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.OptionSelectBsBinding
import com.aks.doggydoo.onboarding.adapter.BottomSheetOptionAdapter
import com.aks.doggydoo.onboarding.viewmodel.OnBoardingViewModel
import com.aks.doggydoo.utils.helper.Result
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "optionBsTag"

@AndroidEntryPoint
class OptionBS(private val showSelectedDataFromOptionBS: (getSelectedValue: String) -> Unit) :
    BottomSheetDialogFragment() {
    private lateinit var binding: OptionSelectBsBinding
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    private var value: String = ""

    private val ageList = listOf("Years", "Months")
    private var breed = ArrayList<String>()
    private var breedIdList = ArrayList<String>()
    private val gender = listOf("Male", "Female")
    private val weight = listOf("Kgs", "Lbs")
    //private val newsfeed = listOf("My Friends", "People Nearby", "Explore")
    private val newsfeed = listOf("All","My Friends")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OptionSelectBsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //this tag's value came from "DogInfoFragment" and I can show the list according to it

        binding.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d(TAG, "onViewCreated: $newVal")
        }
        binding.confirmButton.setOnClickListener {
            if (tag == "age") {
                showSelectedDataFromOptionBS(requireActivity().resources.getStringArray(R.array.picker)[binding.numberPicker.value])
            } else if (tag == "weight") {
                showSelectedDataFromOptionBS(requireActivity().resources.getStringArray(R.array.weight_picker)[binding.numberPicker.value])
            } else
                showSelectedDataFromOptionBS(value)
            dismiss()
        }
        if (tag!!.contains("breedType")) {
            binding.bottomTitle.text = "Choose Pet's Breed"
            getPetBreedData()
        } else {
            when (tag) {
                "gender" -> {
                    binding.bottomTitle.text = "Choose Pet's Gender"
                    binding.optionsRv.adapter =
                        BottomSheetOptionAdapter(requireActivity(), gender) {
                            this.value = it
                        }
                }
                "Kgs", "Lbs" -> {
                    binding.bottomTitle.text = "Choose Pet's Weight"
                    val adapter = BottomSheetOptionAdapter(requireActivity(), weight) {
                        this.value = it
                    }
                    adapter.callOnlyForSelectedWeightType(tag.toString())
                    binding.optionsRv.adapter = adapter
                }
                "age" -> {
                    binding.optionsRv.hide()
                    binding.numberPicker.show()
                    binding.numberPicker.maxValue = 30
                    binding.numberPicker.minValue = 0
                    binding.numberPicker.displayedValues =
                        requireActivity().resources.getStringArray(R.array.picker)
                }
                "weight" -> {
                    binding.bottomTitle.text = "Choose Pet's Weight"
                    binding.optionsRv.hide()
                    binding.numberPicker.show()
                    binding.numberPicker.maxValue = 61
                    binding.numberPicker.minValue = 0
                    binding.numberPicker.displayedValues =
                        requireActivity().resources.getStringArray(R.array.weight_picker)
                }
                "All","My Friends" -> {
                    binding.bottomTitle.text = "Newsfeed View"
                    binding.view.hide()
                    binding.confirmButton.hide()
                    val adapter = BottomSheetOptionAdapter(requireActivity(), newsfeed) {
                        showSelectedDataFromOptionBS(it)
                    }
                    adapter.callSelectedBreed(tag.toString())
                    binding.optionsRv.adapter = adapter
                }
                else -> {
                    val adapter = BottomSheetOptionAdapter(requireActivity(), ageList) {
                        this.value = it
                    }
                    adapter.callOnlyForSelectedAgeType(tag.toString())
                    binding.optionsRv.adapter = adapter
                }
            }
        }
    }

    private fun getPetBreedData() {
        onBoardingViewModel.getPetBreedData().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.layoutBottomSheet)
                        return@Observer
                    }
                    for (category in it.data.petbreeddetails) {
                        breed.add(category.category)
                        breedIdList.add(category.id)
                    }
                    val adapter =
                        BottomSheetOptionAdapter(requireActivity(), breed) { selectedOption ->
                            this.value = selectedOption + breedIdList[breed.indexOf(selectedOption)]
                        }
                    val valueOfEditText = tag!!.replace("breedType ", "")
                    adapter.callSelectedBreed(valueOfEditText)
                    binding.optionsRv.adapter = adapter
                    binding.progressBar.hide()
                    binding.optionsRv.show()
                    binding.view.show()
                    binding.confirmButton.show()
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.layoutBottomSheet)
                }
            }
        })
    }
}