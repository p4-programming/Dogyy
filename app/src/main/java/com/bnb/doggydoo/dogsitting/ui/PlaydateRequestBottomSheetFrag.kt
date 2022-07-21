package com.bnb.doggydoo.dogsitting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bnb.doggydoo.databinding.PlayDateRequestBottomSheetBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaydateRequestBottomSheetFrag(): BottomSheetDialogFragment() {
    private lateinit var binding: PlayDateRequestBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlayDateRequestBottomSheetBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding.no.setOnClickListener {
            dismiss()
        }
        binding.yes.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}