package com.aks.doggydoo.adoption.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aks.doggydoo.adoptdogdetails.ui.AdoptDogDetailActivity
import com.aks.doggydoo.adoption.adapter.AdoptionAdapter
import com.aks.doggydoo.adoption.adapter.ShelterAdapter
import com.aks.doggydoo.adoption.viewmodel.AdoptionModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.FragmentAdoptionMainBinding
import com.aks.doggydoo.postadoptiondetail.ui.PostAdoption
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdoptionMainFragment : Fragment() {
    private lateinit var binding: FragmentAdoptionMainBinding
    private lateinit var adapter: AdoptionAdapter
    private lateinit var shelterAdapter: ShelterAdapter
    private lateinit var adoptionModel: AdoptionModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAdoptionMainBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInit()
        callGetAdoptionListApi()
    }

    private fun getInit() {
        adoptionModel = ViewModelProvider(this).get(AdoptionModel::class.java)
        adapter = AdoptionAdapter(requireContext()) { it, id ->
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                it,
                it.transitionName
            )

            startActivity(
                Intent(requireContext(), AdoptDogDetailActivity::class.java)
                    .putExtra("adoptId", id),
                option.toBundle()
            )
        }
        binding.dogAdoptRv.adapter = adapter

        shelterAdapter = ShelterAdapter(requireContext())
        binding.shelderRv.adapter = shelterAdapter

        binding.backButton.setOnClickListener { requireActivity().finish() }
        binding.add.setOnClickListener {
            startActivity(Intent(requireContext(), PostAdoption::class.java))
        }
        binding.searchview.setOnClickListener {
            binding.searchview.onActionViewExpanded()
            binding.searchText.hide()
        }
        binding.searchview.setOnSearchClickListener {
            binding.searchText.hide()
        }
        binding.searchview.setOnCloseListener {
            binding.searchview.onActionViewCollapsed()
            binding.searchText.show()
            true
        }
        binding.adoptionViewAll.setOnClickListener {
            startActivity(
                Intent(requireActivity(), AdoptionViewAll::class.java)
                    .putExtra("from", "adoption").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
        binding.nearbyShelterViewAll.setOnClickListener {
            startActivity(
                Intent(requireActivity(), AdoptionViewAllShelter::class.java)
                    .putExtra("from", "shelter").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.llAdoptionGuide.visibility = View.GONE
        binding.add.setOnClickListener {
            binding.llAdoptionGuide.visibility = View.VISIBLE
        }

        binding.tvCancel.setOnClickListener {
            binding.llAdoptionGuide.visibility = View.GONE
        }

        binding.tvConfirm.setOnClickListener {
            binding.llAdoptionGuide.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        callGetAdoptionListApi()
    }

    private fun callGetAdoptionListApi() {
        System.out.println("user id>>" + MyApp.getSharedPref().userId)
        adoptionModel.getAdoptionList(MyApp.getSharedPref().userId, MyApp.getSharedPref().userLat, MyApp.getSharedPref().userLong)
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
                        adapter.submitList(it.data.adoptionlist)
                        shelterAdapter.submitList(it.data.shelterList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }
}