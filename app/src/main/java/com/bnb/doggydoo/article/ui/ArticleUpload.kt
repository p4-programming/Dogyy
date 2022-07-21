package com.bnb.doggydoo.article.ui

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.article.viewmodel.ArticleViewModel
import com.bnb.doggydoo.commonutility.*
import com.bnb.doggydoo.databinding.ActivityArticleUploadBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleUpload : AppCompatActivity() {
    private var uri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var binding: ActivityArticleUploadBinding
    private lateinit var articleViewModel: ArticleViewModel
    private  val requestArray = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleUploadBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        ActivityCompat.requestPermissions(
            this,
            requestArray,
            1
        )

        binding.ivbackButton.setOnClickListener { finish() }

        binding.picText.setOnClickListener {
            openGallery()
        }
        binding.tvUploadArticle.setOnClickListener {
            if (checkValidation())
                callInsertArticleAPI()
        }
    }

    private fun checkValidation(): Boolean {
        if (binding.article.text.isEmpty()) {
            CommonMethod.showSnack(binding.parent, "Please mention your article")
            return false
        }
        return true
    }

    private fun callInsertArticleAPI() {
        articleViewModel.getInsertedArticleData(
            MyApp.getSharedPref().userId,
            MultipartFile.prepareFilePart(this, "photo", uri),
            binding.caption.text.toString().trim(),
            binding.article.text.toString().trim(),
            "1"
        ).observe(this, Observer {
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
                    finish()
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
                        val source = ImageDecoder.createSource(contentResolver, data!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        binding.pic.setImageBitmap(bitmap)
                    } else {

                        bitmap = MediaStore.Images.Media.getBitmap(
                            contentResolver,
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