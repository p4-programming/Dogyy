package com.aks.doggydoo.homemodule.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aks.doggydoo.PaymentCard
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.FragmentPaymentBinding

class PaymentFrag : Fragment(R.layout.fragment_payment) {
    private lateinit var binding: FragmentPaymentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCard.setOnClickListener {
            startActivity(Intent(requireContext(),PaymentCard::class.java))
        }
    }
}