package com.aks.doggydoo.sos.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.aks.doggydoo.databinding.FragmentSosMainBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.MyApp

class SOSMainFragment : Fragment() {
    private lateinit var binding: FragmentSosMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding = FragmentSosMainBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvCurrentLocation.setOnClickListener {
                if (binding.rbCheck.isChecked) {
                    startActivity(
                        Intent(requireContext(), ConfirmsoslocationActivity::class.java)
                            .putExtra("pin_lat", MyApp.getSharedPref().userLat)
                            .putExtra("pin_long", MyApp.getSharedPref().userLong)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } else {
                    Toast.makeText(
                      context,
                        "Please check terms and conditions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            tvDropPin.setOnClickListener {
                if (binding.rbCheck.isChecked) {
                    startActivity(
                        Intent(requireContext(), SOSMapActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please check terms and conditions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            ivBack.setOnClickListener {
                requireActivity().finish()
            }

        }
    }
}