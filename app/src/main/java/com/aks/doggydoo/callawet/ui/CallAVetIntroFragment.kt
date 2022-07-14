package com.aks.doggydoo.callawet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.databinding.FragmentCallAVetIntroBinding
import com.aks.doggydoo.utils.CommonMethod

class CallAVetIntroFragment : Fragment() {

    private lateinit var binding: FragmentCallAVetIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding = FragmentCallAVetIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.callwetGif.loadImageAsGif(requireActivity(), R.raw.callwet)
    }
}