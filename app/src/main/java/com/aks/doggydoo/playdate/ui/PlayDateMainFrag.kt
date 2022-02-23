package com.aks.doggydoo.playdate.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.FragmentPlayDateMainBinding
import com.aks.doggydoo.playdate.adapter.*
import com.aks.doggydoo.playdate.viewmodel.PlayDateViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayDateMainFrag : Fragment() {
    private lateinit var binding: FragmentPlayDateMainBinding
    private lateinit var upcomingPet: UpcomingPlaydateAdapter
    private lateinit var invitePet: PlaydateInviteAdapter
    private lateinit var lastPet: LastPlaydateAdapter
    private lateinit var nearbypet: NearbyPetAdapter
    private lateinit var suitablepet: SuitablePetAdapter
    private val playDateViewModel: PlayDateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayDateMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callHomeApi()
    }

    private fun getInit() {
        upcomingPet = UpcomingPlaydateAdapter(requireContext())
        invitePet = PlaydateInviteAdapter(requireContext())
        lastPet = LastPlaydateAdapter(requireContext())
        nearbypet = NearbyPetAdapter(requireContext())
        suitablepet = SuitablePetAdapter(requireContext())

        binding.rvUpcomingPets.adapter = upcomingPet
        binding.rvInvites.adapter = invitePet
        binding.rvLastPets.adapter = lastPet
        binding.rvNearbyPets.adapter = nearbypet
        binding.rvSuitablePets.adapter = suitablepet

        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.tvViewAllUpcoming.setOnClickListener {
            navigateTo("upcoming")
        }
        binding.tvViewAllInvites.setOnClickListener {
            navigateTo("invite")
        }
        binding.tvViewAllNear.setOnClickListener {
            navigateTo("nearby")
        }
        binding.tvViewAllLast.setOnClickListener {
            navigateTo("last")
        }
        binding.tvViewAllSuitable.setOnClickListener {
            navigateTo("suitable")
        }
    }

    override fun onResume() {
        super.onResume()
        callHomeApi()
    }

    private fun navigateTo(type: String) {
        startActivity(
            Intent(requireContext(), ViewAllPlayDateActivity::class.java)
                .putExtra("type", type).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun callHomeApi() {
        System.out.println("current lat>>"+ MyApp.getSharedPref().userLat)
        System.out.println("current lat>>"+ MyApp.getSharedPref().userLong)
        playDateViewModel.getHomeData(MyApp.getSharedPref().userId,MyApp.getSharedPref().userLat,MyApp.getSharedPref().userLong)
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data!!.responseCode == "0") {
                            it.data.responseMessage.snack(Color.RED, binding.root)
                            return@Observer
                        }
                        if (it.data.upcomingList.isEmpty()) {
                            binding.noUpcoming.show()
                        }else{
                            binding.noUpcoming.hide()
                            upcomingPet.submitList(it.data.upcomingList)
                        }
                        if (it.data.invitesList.isEmpty()) {
                            binding.noInvites.show()
                        }else{
                            binding.noInvites.hide()
                            invitePet.submitList(it.data.invitesList)
                        }

                        if (it.data.lastPlayDateList.isEmpty()) {
                            binding.noLast.show()
                        }else{
                            binding.noLast.hide()
                            lastPet.submitList(it.data.lastPlayDateList)
                        }
                        if (it.data.nearbypets.isEmpty()) {
                            binding.noNearBy.show()
                        }else{
                            binding.noNearBy.hide()
                            nearbypet.submitList(it.data.nearbypets)
                        }

                        if (it.data.suitableMateList.isEmpty()) {
                            binding.noSuitable.show()
                        }else{
                            binding.noSuitable.hide()
                            suitablepet.submitList(it.data.suitableMateList)
                        }

                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
    }
}