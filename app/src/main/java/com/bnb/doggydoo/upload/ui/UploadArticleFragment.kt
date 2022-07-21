package com.bnb.doggydoo.upload.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bnb.doggydoo.R
import com.bnb.doggydoo.article.viewmodel.ArticleViewModel
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.FragmentUploadArticleBinding
import com.bnb.doggydoo.newsfeed.ui.NewsFeedDashboardActivity
import com.bnb.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadArticleFragment : Fragment() {
    private var _binding: FragmentUploadArticleBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private lateinit var bitmap: Bitmap

    private val articleViewModel: ArticleViewModel by viewModels()
    private val newsFeedViewModel: NewsFeedViewModel by viewModels()

    private val requestArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUploadArticleBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityCompat.requestPermissions(
            requireActivity(),
            requestArray,
            1
        )


        binding.picText.setOnClickListener {
            openGallery()
        }
        binding.tvUploadArticle.setOnClickListener {
            //   callInsertNewsFeedAPI()
            if (binding.article.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please mention your article.", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.caption.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please mention your caption.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                callInsertArticleAPI()
            }

        }
    }


    private fun callInsertArticleAPI() {
        newsFeedViewModel.uploadFeed(
            MyApp.getSharedPref().userId,
            binding.caption.text.toString().trim(),
            binding.article.text.toString().trim(),
            "image",
            MultipartFile.prepareFilePart(requireContext(), "photo", uri),
            "1"
        ).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                    binding.tvUploadArticle.isEnabled = false
                }
                Result.Status.SUCCESS -> {
                    binding.tvUploadArticle.isEnabled = true
                    binding.progressBar.hide()
                    if (it.data?.responseCode?.equals("0")!!) {
                        it.data.responseMessage.snack(Color.RED, binding.parent)
                        return@Observer
                    }
                    "Article Uploaded Successfully".snack(R.color.docbuton, binding.parent)
                    startActivity(
                        Intent(requireContext(), NewsFeedDashboardActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                }
                Result.Status.ERROR -> {
                    binding.tvUploadArticle.isEnabled = true
                    binding.progressBar.hide()
                    it.message?.snack(Color.RED, binding.parent)
                    binding.pic.hide()
                    binding.picText.show()
                }
            }
        })
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    uri = result.data?.data
                    val data = result.data!!.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source =
                            ImageDecoder.createSource(requireActivity().contentResolver, data!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        binding.pic.setImageBitmap(bitmap)
                    } else {

                        bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            result.data?.data
                        )
                        binding.pic.setImageURI(result.data?.data)
                    }
                    binding.pic.show()
                    binding.picText.hide()
                }
            }
        }

    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            this.type = "image/*"
            resultLauncher.launch(this)
        }
    }
}