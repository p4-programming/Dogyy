package com.bnb.doggydoo.newsfeed.ui

import android.content.Intent
import android.graphics.Color
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityUploadPhotoBinding
import com.bnb.doggydoo.mydog.ui.MyDog
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.newsfeed.viewmodel.NewsFeedViewModel
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.videocall.ApplicationController.context
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPhotoActivity : AppCompatActivity() {
    private var _binding: ActivityUploadPhotoBinding? = null
    private val binding get() = _binding!!

    private lateinit var myDogViewModel: MyDogViewModel
    private lateinit var newsFeedViewModel: NewsFeedViewModel
    private var uri: Uri? = null
    private var petId = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()


    }

    private fun getThumbnail(uri: Uri) {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        val bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MICRO_KIND)
        binding.image.setImageBitmap(bitmap)
    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        newsFeedViewModel = ViewModelProvider(this).get(NewsFeedViewModel::class.java)

        if (intent.hasExtra("petId"))
            petId = intent.getStringExtra("petId")!!

        uri = Uri.parse(intent.getStringExtra("uri"))

    //    getThumbnail(Uri.parse(intent.getStringExtra("uri")))

        type = intent.getStringExtra("type").toString()
        if (uri != null) {
            if (type == "image") {
                binding.image.setImageURI(uri)
            } else {
                binding.image.setImageResource(R.drawable.ic_baseline_videocam_24)
            }
        }
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.tvUploadArticle.setOnClickListener {
            if (binding.caption.text.isEmpty()) {
                Toast.makeText(this, "Please mention a caption for this.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                callUploadDocAPI()
            }
        }
    }

    private fun callUploadDocAPI() {
        if (petId != "") {

            System.out.println("data is>>" + petId)
            System.out.println("data is>>" + binding.caption.text.toString().trim())
            System.out.println("data is>>" + uri.toString())

            myDogViewModel.getPostPetDocumentData(
                petId, binding.caption.text.toString().trim(),
                MultipartFile.prepareFilePart(this, "document", uri)
            ).observe(this, Observer {
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
                        startActivity(
                            Intent(this, MyDog::class.java)
                                .putExtra("petId", petId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
        } else {
            newsFeedViewModel.uploadFeed(
                MyApp.getSharedPref().userId, binding.caption.text.toString().trim(),
                "", "image",
                MultipartFile.prepareFilePart(this, "photo", uri), "0"
            ).observe(this, Observer {
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
                        startActivity(
                            Intent(this, NewsFeedDashboardActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message!!.snack(Color.RED, binding.root)
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}