package com.bnb.doggydoo.sos.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentSosMainBinding
import com.bnb.doggydoo.homemodule.ui.HomeActivity
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SOSMainFragment : Fragment() {
    private lateinit var binding: FragmentSosMainBinding
    private val myDogViewModel: MyDogViewModel by viewModels()
    private var uri: Uri? = null
    private val args : SOSMainFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding = FragmentSosMainBinding.inflate(layoutInflater)

        HomeActivity.menuIcon.visibility = View.GONE

        val pin_latitude = args.lattitude
        val pin_longitude = args.longitude
        if(!pin_latitude.isNullOrEmpty() && !pin_longitude.isNullOrEmpty()){
            addDistressPetAPI(pin_latitude,pin_longitude)
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val pinLatitude = arguments!!.getString("latitude")

        binding.apply {
            tvCurrentLocation.setOnClickListener {
                if (binding.rbCheck.isChecked) {
                    addDistressPetAPI(MyApp.getSharedPref().userLat,MyApp.getSharedPref().userLong)

//                    startActivity(
//                        Intent(requireContext(), ConfirmsoslocationActivity::class.java)
//                            .putExtra("pin_lat", MyApp.getSharedPref().userLat)
//                            .putExtra("pin_long", MyApp.getSharedPref().userLong)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    )
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

                    requireView().findNavController().navigate(R.id.action_SOSMainFragment_to_pin_Map_Fragment)
//                    startActivity(
//                        Intent(requireContext(), SOSMapActivity::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    )
                   // Log.d("TAG", "onViewCreated: "+ pinLatitude)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please check terms and conditions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            ivBack.setOnClickListener {
                requireView().findNavController().popBackStack()
            }

        }
    }

    private fun addDistressPetAPI(pinLatitude:String,pinLongitude:String) {
        myDogViewModel.getDistressPetData(
            MyApp.getSharedPref().userId,
            "current description",
            pinLatitude,
            pinLongitude,
            MultipartFile.prepareFilePart(requireContext(), "pet_image[]", uri)
        )
            .observe(requireActivity(), Observer {
                when(it.status){
                    Result.Status.LOADING ->{
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS ->{
                        binding.progressBar.hide()
                        it.data?.responseMessage?.snack(
                            Color.BLACK,
                            binding.parent
                        )
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        confirmDialog()
                    }
                    Result.Status.ERROR ->{
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    fun confirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("SOS")
        builder.setMessage("Thank you for your contribution towards helping a pet/stray in need")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

//        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//
//            startActivity(
//                Intent(
//                    this,
//                    HomeActivity::class.java
//                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            )
//            finish()
//        }

        /* builder.setNegativeButton(android.R.string.no) { dialog, which ->
             Toast.makeText(
                 applicationContext,
                 android.R.string.no, Toast.LENGTH_SHORT
             ).show()
         }

         builder.setNeutralButton("Maybe") { dialog, which ->
             Toast.makeText(
                 applicationContext,
                 "Maybe", Toast.LENGTH_SHORT
             ).show()
         }*/
        builder.show()
    }
}