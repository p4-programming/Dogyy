package com.bnb.doggydoo.dogsitting.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentDogSittingMainBinding
import com.bnb.doggydoo.dogsitting.DogsittingDogsatAdapter
import com.bnb.doggydoo.dogsitting.DogsittingHerosAdapter
import com.bnb.doggydoo.dogsitting.adapter.InviteDogAdapter
import com.bnb.doggydoo.dogsitting.adapter.UpcomingDogAdapter
import com.bnb.doggydoo.dogsitting.datasource.model.DogSittingHomeResponse
import com.bnb.doggydoo.dogsitting.viewmodel.DogsittingModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogSittingMainFragment : Fragment() {
    private lateinit var binding: FragmentDogSittingMainBinding
    private lateinit var dogsittingmodel: DogsittingModel
    private lateinit var nearbyAdapter: UpcomingDogAdapter
    private lateinit var inviteAdapter: InviteDogAdapter
    private lateinit var herosAdapter: DogsittingHerosAdapter
    private lateinit var recentdogsitAdapter: DogsittingDogsatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for requireActivity() fragment
        binding = FragmentDogSittingMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonMethod.makeTransparentStatusBar(activity?.window)
        getInit()
        callDogsittingAPI()
    }

    private fun getInit() {
        dogsittingmodel = ViewModelProvider(this).get(DogsittingModel::class.java)

        nearbyAdapter = UpcomingDogAdapter(requireActivity())
        binding.rvNearbyPets.adapter = nearbyAdapter
        inviteAdapter= InviteDogAdapter(requireActivity())
        binding.rvDogsittingInvites.adapter =inviteAdapter
        herosAdapter = DogsittingHerosAdapter(requireActivity())
        binding.rvDogsittingHeros.adapter = herosAdapter
        recentdogsitAdapter = DogsittingDogsatAdapter(requireActivity())
        binding.rvRecentlyDogsat.adapter = recentdogsitAdapter

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
        binding.tvViewAllRecentDogsit.setOnClickListener {
            navigateTo("recent")
        }
    }

    override fun onResume() {
        super.onResume()
        callDogsittingAPI()
    }

    private fun navigateTo(type: String) {
        startActivity(
            Intent(context, ViewAllDogsitActivity::class.java).
            putExtra("viewType", type).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun callDogsittingAPI() {
        dogsittingmodel.getDogsittingHomeList(MyApp.getSharedPref().userId, MyApp.getSharedPref().userLat,MyApp.getSharedPref().userLong)
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

    private fun renderValues(data: DogSittingHomeResponse?) {
        if (data != null) {
            if (data.nearbydogsitpetlist.size > 0) {
                binding.tvAlert1.visibility = View.GONE
                nearbyAdapter.submitList(data.nearbydogsitpetlist)
            } else {
                binding.tvAlert1.visibility = View.VISIBLE
            }
        }


        if (data != null) {
            if (data.dogsittinginvitesList.size > 0) {
                binding.tvAlert2.visibility = View.GONE
                inviteAdapter.submitList(data.dogsittinginvitesList)
            } else {
                binding.tvAlert2.visibility = View.VISIBLE
            }
        }

        if (data != null) {
            if (data.herosnearbydogsitlist.size > 0) {
                binding.tvAlert3.visibility = View.GONE
                herosAdapter.submitList(data.herosnearbydogsitlist)
            } else {
                binding.tvAlert3.visibility = View.VISIBLE
            }
        }

        if (data != null) {
            if (data.recentdogsitlist.size > 0) {
                binding.tvAlert4.visibility = View.GONE
                recentdogsitAdapter.submitList(data.recentdogsitlist)
            } else {
                binding.tvAlert4.visibility = View.VISIBLE
            }
        }
    }
}