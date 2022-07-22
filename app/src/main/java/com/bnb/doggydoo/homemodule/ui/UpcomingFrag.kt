package com.bnb.doggydoo.homemodule.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentCalendarBinding
import com.bnb.doggydoo.homemodule.adapter.LostFoundAdapter
import com.bnb.doggydoo.homemodule.adapter.PlayDateAdapter
import com.bnb.doggydoo.homemodule.adapter.ReminderAdapter
import com.bnb.doggydoo.homemodule.viewmodel.HomeViewModel
import com.bnb.doggydoo.playdate.ui.ViewAllPlayDateActivity
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingFrag : Fragment(R.layout.fragment_calendar) {
    private lateinit var binding: FragmentCalendarBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var lostfoundAdapter: LostFoundAdapter
    private lateinit var playdateAdapter: PlayDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HomeActivity.menuIcon.visibility = View.GONE

        reminderAdapter =  ReminderAdapter(requireContext())
        binding.reminderRv.adapter = reminderAdapter

      /*  lostfoundAdapter = LostFoundAdapter(requireContext())
        binding.LostFoundrv.adapter = lostfoundAdapter*/

        playdateAdapter = PlayDateAdapter(requireContext())
        binding.Playdatesrv.adapter = playdateAdapter

        getUserUpcomingApi()
        
        binding.ivBack.setOnClickListener(){
            HomeActivity.menuIcon.visibility = View.VISIBLE
            requireView().findNavController().popBackStack()
        }

        binding.viewAllPlaydated.setOnClickListener {
            startActivity(
                Intent(requireContext(), ViewAllPlayDateActivity::class.java)
                    .putExtra("type", "upcoming").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.viewAllReminder.setOnClickListener {
            startActivity(
                Intent(requireContext(), ViewAllReminderActivity::class.java)
                    .putExtra("type", "reminder").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun getUserUpcomingApi() {
        println("user id>>"+ MyApp.getSharedPref().userId)
        homeViewModel.getUserUpcomingResponse(MyApp.getSharedPref().userId)
            .observe(viewLifecycleOwner,
                Observer {
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

                            if (it.data.userPetpayList.isNotEmpty()){
                                binding.noDatePlayDate.visibility =View.GONE
                                playdateAdapter.submitList(it.data.userPetpayList)
                            }else{
                                binding.noDatePlayDate.visibility =View.VISIBLE
                            }

                            if (it.data.userReminderList.isNotEmpty()){
                                binding.noDateReminder.visibility =View.GONE
                                reminderAdapter.submitList(it.data.userReminderList)
                            }else{
                                binding.noDateReminder.visibility =View.VISIBLE
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