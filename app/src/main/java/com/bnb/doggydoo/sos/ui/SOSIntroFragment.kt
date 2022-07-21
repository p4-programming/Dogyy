package com.bnb.doggydoo.sos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnb.doggydoo.R
import com.bnb.doggydoo.utils.CommonMethod

class SOSIntroFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        CommonMethod.makeTransparentStatusBar(activity?.window)
        return inflater.inflate(R.layout.fragment_sos_intro, container, false)
    }
}