package com.bnb.doggydoo.sos.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.databinding.FragmentSOSDistressBinding
import com.bnb.doggydoo.homemodule.ui.HomeActivity
import com.bnb.doggydoo.utils.CommonMethod
import java.io.ByteArrayOutputStream


class SOSDistressFragment : Fragment() {
    private var _binding: FragmentSOSDistressBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val REQUEST_PERMISSION = 100
    private var uri: Uri? = null
    private lateinit var navController: NavController
    private final val REQUEST_IMAGE_CAPTURE = 1475357526
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CommonMethod.makeTransparentStatusBar(activity?.window)
        _binding = FragmentSOSDistressBinding.inflate(inflater, container, false)
        val view = binding.root
        HomeActivity.menuIcon.visibility = View.GONE
        binding.b2.isChecked=true

        getInit()
        setRadiogroup()

        binding.ivDog.setOnClickListener({
            binding.getImageFromGallery.performClick()
        })

        binding.ivBack.setOnClickListener(){
            HomeActivity.menuIcon.visibility = View.VISIBLE
            requireView().findNavController().popBackStack()
        }

        return view

    }

    private fun setRadiogroup() {
        binding.b1.setOnClickListener(){
            binding.b1.isChecked=true
            binding.rg2.clearCheck()
            binding.rg3.clearCheck()
        }
        binding.b2.setOnClickListener(){
            binding.b2.isChecked=true
            binding.rg2.clearCheck()
            binding.rg3.clearCheck()
        }
        binding.b3.setOnClickListener(){
            binding.b3.isChecked=true
            binding.rg1.clearCheck()
            binding.rg3.clearCheck()
        }
        binding.b4.setOnClickListener(){
            binding.b4.isChecked=true
            binding.rg1.clearCheck()
            binding.rg3.clearCheck()
        }
        binding.b5.setOnClickListener(){
            binding.b5.isChecked=true
            binding.rg1.clearCheck()
            binding.rg2.clearCheck()
        }
        binding.b6.setOnClickListener(){
            binding.b6.isChecked=true
            binding.rg1.clearCheck()
            binding.rg2.clearCheck()
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun getInit() {
        binding.tvCancel.setOnClickListener {
            HomeActivity.menuIcon.visibility = View.VISIBLE
            requireView().findNavController().popBackStack()
            Toast.makeText(requireContext(), "canceled", Toast.LENGTH_SHORT).show()
        }

        binding.tvConfirm.setOnClickListener {
            if (binding.etPetDescription.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please add description.", Toast.LENGTH_SHORT).show()
            }else{

//                val transaction = childFragmentManager?.beginTransaction()
//                transaction?.replace(R.id.SOSDistressFragment, SOSMainFragment())
//                transaction?.disallowAddToBackStack()
//                transaction?.commit()

                val action = SOSDistressFragmentDirections.actionSOSDistressFragmentToSOSMainFragment(null,null)
                requireView().findNavController().navigate(action)
            }

        }

        binding.getImageFromGallery.setOnClickListener {
            ChooseOption()
        }
    }

    private fun ChooseOption() {
        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.choose_image_option)

        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val camera = dialog.findViewById<View>(R.id.ivCamera) as ImageView
        val gallery = dialog.findViewById<View>(R.id.ivGallery) as ImageView

        camera.setOnClickListener {

            dispatchTakePictureIntent()

//            ImagePicker.with(this)
//                .cameraOnly()
//                .crop()                    //Crop image(Optional), Check Customization for more option
//                .compress(500)            //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(
//                    300,
//                    300
//                )    //Final image resolution will be less than 1080 x 1080(Optional)
//                .start()

            dialog.dismiss()
        }

        gallery.setOnClickListener {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                this.type = "image/*"
                resultLauncher.launch(this)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            requireActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
             Toast.makeText(requireContext(),"error in camera",Toast.LENGTH_LONG).show()
            // display error state to the user
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            binding.ivDog.setImageBitmap(imageBitmap)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val bmp = data?.extras!!["data"] as Bitmap?
                val stream = ByteArrayOutputStream()
                bmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()

                // convert byte array to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(
                    byteArray, 0,
                    byteArray.size
                )
               // binding.llUploadPic.visibility = View.GONE
                binding.ivDog.setImageBitmap(bitmap)
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    uri = result.data?.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source =
                            ImageDecoder.createSource(activity!!.contentResolver, uri!!)
                        binding.ivDog.setImageBitmap(ImageDecoder.decodeBitmap(source))
                        binding.ivDog.visibility=View.VISIBLE
                        binding.llUploadPic.hide()
                    } else {
                        binding.ivDog.setImageURI(uri)
                        binding.ivDog.visibility=View.VISIBLE
                        binding.llUploadPic.hide()
                    }
                }
            }
        }


    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

