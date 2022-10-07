package com.bnb.doggydoo.sos.ui

import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SOSMainFragment : Fragment() {
    private lateinit var binding: FragmentSosMainBinding
    private val myDogViewModel: MyDogViewModel by viewModels()
    private var uri: Uri? = null
    private val args: SOSMainFragmentArgs by navArgs()
    private var hashMap:HashMap<String,String> = HashMap<String,String>()
    private var notificationType = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        CommonMethod.makeTransparentStatusBar(activity?.window)

        binding = FragmentSosMainBinding.inflate(layoutInflater)

        HomeActivity.menuIcon.visibility = View.GONE

        val pin_latitude = args.lattitude
        val pin_longitude = args.longitude
        val notificationType = args.notificationType

        if (!pin_latitude.isNullOrEmpty() && !pin_longitude.isNullOrEmpty()) {
            //addDistressPetFirebase(CommonMethod.getTimeStamp(),pin_latitude, pin_longitude)
            addDistressPetAPI(pin_latitude, pin_longitude, notificationType)
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
                    if(binding.sendAll.isChecked){
                        //addDistressPetFirebase(CommonMethod.getTimeStamp(),MyApp.getSharedPref().userLat, MyApp.getSharedPref().userLong)

                        notificationType = "1"
                         addDistressPetAPI(
                             MyApp.getSharedPref().userLat,
                             MyApp.getSharedPref().userLong,
                             notificationType
                         )

                    }else if (binding.sendToFrd.isChecked){
                        //addDistressPetFirebase(CommonMethod.getTimeStamp(),MyApp.getSharedPref().userLat, MyApp.getSharedPref().userLong)

                        notificationType = "0"
                        addDistressPetAPI(
                            MyApp.getSharedPref().userLat,
                            MyApp.getSharedPref().userLong,
                            notificationType
                        )

                    }else{
                        Toast.makeText(context,"Please select at least one",Toast.LENGTH_SHORT).show()
                    }
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

                    val petDescription = args.description
                    val distressType = args.type
                    val uri = args.imageUri

                    val action = SOSMainFragmentDirections.actionSOSMainFragmentToPinMapFragment(petDescription,distressType,uri.toString(),notificationType)
                    requireView().findNavController().navigate(action)
                    Log.d("Deepak","Action : $action")

//                    requireView().findNavController()
//                        .navigate(R.id.action_SOSMainFragment_to_pin_Map_Fragment)
//                    startActivity(
//                        Intent(requireContext(), SOSMapActivity::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    )
//                    Log.d("TAG", "onViewCreated: "+ pinLatitude)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please check terms and conditions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            ivBack.setOnClickListener {
                //requireView().findNavController() .navigate(R.id.action_SOSMainFragment_to_SOSDistressFragment)
                requireView().findNavController().popBackStack()
            }

        }
    }

    private fun addDistressPetFirebase(timestamp:String, pinLatitude: String, pinLongitude: String) {

        val petDescription = args.description
        val distressType = args.type

        hashMap.put("user_id",MyApp.getSharedPref().userId)
        hashMap.put("pet_description",petDescription)
        hashMap.put("lattitude",pinLatitude)
        hashMap.put("longitude",pinLongitude)
        hashMap.put("type",distressType)
        hashMap.put("date",CommonMethod.getDate())

        FirebaseDatabase.getInstance().reference.child("DogDistress").child(timestamp)
            .setValue(hashMap)
            .addOnCompleteListener{task->
            if (task.isSuccessful) {
                Log.d("TAG", "addDistressPetFirebase: added")
                customDialog()
            } else {
                task.exception?.let {
                    Log.d("TAG", "addDistressPetFirebase: error")
                }
            }
        }
    }

    private fun addDistressPetAPI(
        pinLatitude: String,
        pinLongitude: String,
        notificationType: String
    ) {
        myDogViewModel.getDistressPetData(
            MyApp.getSharedPref().userId,
            args.description,
            pinLatitude,
            pinLongitude,
            MultipartFile.prepareFilePart(requireContext(), "pet_image[]", uri),
            args.type,
            notificationType
        )
            .observe(requireActivity(), Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
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
                        customDialog()
                        //confirmDialog()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

//    private fun DistressPetRegisterAPI(pinLatitude: String, pinLongitude: String) {
//        myDogViewModel.getDistressPetRegisterData(
//            MyApp.getSharedPref().userId,
//            "current description",
//            pinLatitude,
//            pinLongitude,
//            MultipartFile.prepareFilePart(requireContext(), "pet_image[]", uri),
//            args.type,
//            "0"
//        )
//            .observe(requireActivity(), Observer {
//                when (it.status) {
//                    Result.Status.LOADING -> {
//                        binding.progressBar.show()
//                    }
//                    Result.Status.SUCCESS -> {
//                        binding.progressBar.hide()
//                        it.data?.responseMessage?.snack(
//                            Color.BLACK,
//                            binding.parent
//                        )
//                        if (it.data?.responseCode.equals("0")) {
//                            it.data?.responseMessage?.snack(
//                                Color.RED,
//                                binding.parent
//                            )
//                            return@Observer
//                        }
//                        customDialog()
//                        //confirmDialog()
//                    }
//                    Result.Status.ERROR -> {
//                        binding.progressBar.hide()
//                        it.message?.snack(Color.RED, binding.parent)
//                    }
//                }
//            })
//    }

    private fun customDialog() {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        val btn = mDialogView.findViewById<TextView>(R.id.okBtn)
        btn.setOnClickListener(){
            HomeActivity.menuIcon.visibility = View.VISIBLE
            requireView().findNavController().navigate(R.id.action_SOSMainFragment_to_nav_home3)
            mAlertDialog.dismiss()
        }
    }



    fun confirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("SOS")
        builder.setMessage("Thank you for your contribution towards helping a pet/stray in need")


//        builder.setPositiveButton("OK", DialogInterface.OnClickListener(
//            Toast.makeText(requireContext(),"This is ok",Toast.LENGTH_LONG).show()
//        ))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            HomeActivity.menuIcon.visibility = View.VISIBLE
            requireView().findNavController().navigate(R.id.action_SOSMainFragment_to_nav_home3)
            //finish()
        }

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