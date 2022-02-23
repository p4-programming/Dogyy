package com.aks.doggydoo.parkdescription.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aks.doggydoo.databinding.PlaceCheckInBottomSheetBinding
import com.aks.doggydoo.rateplace.ui.RatePlaceActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaceBottomSheetFrag : BottomSheetDialogFragment() {
    private lateinit var binding: PlaceCheckInBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaceCheckInBottomSheetBinding.inflate(layoutInflater)
        binding.no.setOnClickListener {
            dismiss()
        }
        binding.yes.setOnClickListener {
            dismiss()
//            startActivity(Intent(requireContext(), RatePlaceActivity::class.java))
        }
        return binding.root
    }
}