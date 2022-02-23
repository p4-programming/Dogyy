package com.aks.doggydoo.dogsitting.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aks.doggydoo.databinding.PlayDateRequestBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaydateRequestBottomSheetFrag(): BottomSheetDialogFragment() {
    private lateinit var binding: PlayDateRequestBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlayDateRequestBottomSheetBinding.inflate(layoutInflater)
        binding.no.setOnClickListener {
            dismiss()
        }
        binding.yes.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}