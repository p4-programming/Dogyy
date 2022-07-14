package com.aks.doggydoo.adoption.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aks.doggydoo.R
import com.aks.doggydoo.utils.CommonMethod

class AdoptionIntroFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CommonMethod.makeTransparentStatusBar(activity?.window)
        return inflater.inflate(R.layout.fragment_adoption_intro, container, false)
    }
}