package com.aks.doggydoo.fostering.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.FragmentFosterMainBinding
import com.aks.doggydoo.fostering.adapter.FosterHerosAdapter
import com.aks.doggydoo.fostering.adapter.FosteringFosterAdapter
import com.aks.doggydoo.fostering.adapter.InviteFosterAdapter
import com.aks.doggydoo.fostering.adapter.NearByFosterAdapter
import com.aks.doggydoo.fostering.datasource.model.FosteringHomeResponse
import com.aks.doggydoo.fostering.viewmodel.FosteringModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FosterMainFrag : Fragment() {
    private lateinit var binding: FragmentFosterMainBinding
    private lateinit var nearbypetadapter: NearByFosterAdapter
    private lateinit var inviteFosterAdapter: InviteFosterAdapter
    private lateinit var fosterHerosAdapter: FosterHerosAdapter
    private lateinit var fosteringFosterAdapter: FosteringFosterAdapter
    private lateinit var fosteringmodel: FosteringModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for requireActivity() fragment
        binding = FragmentFosterMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callFosterAPI()
    }

    override fun onResume() {
        super.onResume()
        callFosterAPI()
    }

    private fun getInit() {
        fosteringmodel = ViewModelProvider(this).get(FosteringModel::class.java)
        nearbypetadapter = NearByFosterAdapter(requireActivity())
        inviteFosterAdapter = InviteFosterAdapter(requireActivity())
        fosterHerosAdapter = FosterHerosAdapter(requireActivity())
        fosteringFosterAdapter = FosteringFosterAdapter(requireActivity())
        binding.rvNearbyPets.adapter = nearbypetadapter
        binding.rvFosteringInvites.adapter = inviteFosterAdapter
        binding.rvFosteringHeros.adapter = fosterHerosAdapter
        binding.rvRecentlyFostered.adapter = fosteringFosterAdapter


        binding.backButton.setOnClickListener { requireActivity().finish() }

        binding.tvViewllNearBy.setOnClickListener {
            navigateTo("nearby")
        }
        binding.tvViewAllInvites.setOnClickListener {
            navigateTo("invite")
        }
        binding.tvViewAllHero.setOnClickListener {
            navigateTo("hero")
        }
        binding.tvViewAllRecentFoster.setOnClickListener {
            navigateTo("recent")
        }
    }

    private fun navigateTo(type: String) {
        startActivity(
            Intent(context, ViewAllFosteringActivity::class.java).
                putExtra("viewType", type).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun callFosterAPI() {
        fosteringmodel.getFosteringHomeList(MyApp.getSharedPref().userId,MyApp.getSharedPref().userLat,MyApp.getSharedPref().userLong)
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
                        renderValues(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun renderValues(data: FosteringHomeResponse) {
        if (data.nearbypetslist.size > 0) {
            binding.tvAlert.visibility = View.GONE
            nearbypetadapter.submitList(data.nearbypetslist)
        } else {
            binding.tvAlert.visibility = View.VISIBLE
        }


        if (data.fosteringinvitesList.size > 0) {
            binding.tvAlert1.visibility = View.GONE
            inviteFosterAdapter.submitList(data.fosteringinvitesList)
        } else {
            binding.tvAlert1.visibility = View.VISIBLE
        }

        if (data.heroforsetinglist.size > 0) {
            binding.tvAlert2.visibility = View.GONE
            fosterHerosAdapter.submitList(data.heroforsetinglist)
        } else {
            binding.tvAlert2.visibility = View.VISIBLE
        }

        if (data.Recentltforsetinglist.size > 0) {
            binding.tvAlert3.visibility = View.GONE
            fosteringFosterAdapter.submitList(data.Recentltforsetinglist)
        } else {
            binding.tvAlert3.visibility = View.VISIBLE
        }
    }
}