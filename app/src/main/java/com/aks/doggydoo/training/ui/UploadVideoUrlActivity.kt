package com.aks.doggydoo.training.ui

import android.R
import android.graphics.Color
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityUploadVideoUrlBinding
import com.aks.doggydoo.training.viewmodel.TrainingViewModel
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class UploadVideoUrlActivity : AppCompatActivity() {
    private var _binding: ActivityUploadVideoUrlBinding? = null
    private val binding get() = _binding!!
    private var uploadURL: String = ""
    private var videoId: String? = ""
    private val trainingViewModel: TrainingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUploadVideoUrlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
    }

    private fun getInit() {
        binding.backButton.setOnClickListener {
            finish()
        }



        binding.tvUploadVideo.setOnClickListener {
            if (binding.etCaption.text.isEmpty()) {
                Toast.makeText(this, "Mention the caption.", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.etUrl.text.isEmpty()) {
                    Toast.makeText(this, "Mention the URL.", Toast.LENGTH_SHORT).show()
                } else {
                    uploadURL = binding.etUrl.text.toString().trim()
                    if (URLUtil.isValidUrl(uploadURL)) {
                        videoId = extractVideoId(uploadURL)!!
                        if (videoId != null) {
                            uploadTrainingVideoAPI(videoId!!)
                        } else {
                            Toast.makeText(this, "URL is not correct.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "URL is not correct.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun extractVideoId(ytUrl: String?): String? {
        var videoId: String? = null
        val regex =
            "^((?:https?:)?//)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be|youtube-nocookie.com))(/(?:[\\w\\-]+\\?v=|feature=|watch\\?|e/|embed/|v/)?)([\\w\\-]+)(\\S+)?\$"
        val pattern: Pattern = Pattern.compile(
            regex,
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            videoId = matcher.group(5)
        }
        return videoId
    }

    private fun uploadTrainingVideoAPI(videoId: String) {
        trainingViewModel.uploadTrainingData(
            MyApp.getSharedPref().userId,
            binding.etCaption.text.toString(),
            videoId
        ).observe(this, Observer {
            when (it.status) {
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                }
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    Toast.makeText(this, "Uploaded successfully.", Toast.LENGTH_SHORT).show()
                    finish()
                    if (it.data!!.responseCode == "0") {
                        it.data.responseMessage.snack(Color.RED, binding.root)
                        return@Observer
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