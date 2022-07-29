package com.bnb.doggydoo.callawet.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.callawet.adapter.NearbyDoctorAdapter
import com.bnb.doggydoo.callawet.adapter.NearbyHospitalAdapter
import com.bnb.doggydoo.callawet.viewmodel.CallaWetModel
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentCallAVetMainBinding
import com.bnb.doggydoo.sos.ui.SOSDistressFragment
import com.bnb.doggydoo.sos.ui.SOSIntro
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallAVetMainFragment : Fragment() {
    private lateinit var binding: FragmentCallAVetMainBinding
    private lateinit var adapternerabydoc: NearbyDoctorAdapter
    private lateinit var adapternerabyhos: NearbyHospitalAdapter
    private lateinit var callawetModel: CallaWetModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for requireActivity() fragment
        binding = FragmentCallAVetMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callwetdetailsAPI()
    }

    private fun getInit() {
        callawetModel = ViewModelProvider(this).get(CallaWetModel::class.java)

        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }

        adapternerabyhos = NearbyHospitalAdapter(requireActivity())
        adapternerabydoc = NearbyDoctorAdapter(requireActivity())
        binding.nearbyHospitalRv.adapter = adapternerabyhos
        binding.nearbyDocRv.adapter = adapternerabydoc

        binding.viewAllDoc.setOnClickListener {
            startActivity(Intent(requireContext(), ViewAllVetActivity::class.java)
                .putExtra("type","alldoc")
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.viewAllClinic.setOnClickListener {
            startActivity(Intent(requireContext(), ViewAllVetActivity::class.java)
                .putExtra("type","allclinic")
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.ambulance.setOnClickListener {

            startActivity(Intent(requireContext(),SOSIntro::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private fun callwetdetailsAPI() {
        callawetModel.getCallaWetHomeList(MyApp.getSharedPref().userId, MyApp.getSharedPref().userLat, MyApp.getSharedPref().userLong)
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == ("0")) {
                            it.data.responseMessage.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        adapternerabyhos.submitList(it.data.nearByHosList)
                        adapternerabydoc.submitList(it.data.nearByDoclist)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }
}