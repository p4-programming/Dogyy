package com.aks.doggydoo.training.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.aks.doggydoo.R
import com.aks.doggydoo.databinding.ActivityFullscreenVideoBinding
import com.aks.doggydoo.utils.CommonMethod
import com.aks.doggydoo.utils.network.ApiConstant.BLOG_IMAGE_BASE_URL
import com.halilibo.bvpkotlin.BetterVideoPlayer

class FullScreenVideoActivity : AppCompatActivity() {
    private var _binding: ActivityFullscreenVideoBinding? = null
    private val binding get() = _binding!!
    var video_url = ""
    var caption = ""
    lateinit var player: BetterVideoPlayer

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFullscreenVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        // callTrainingDetailAPI()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getInit() {
        video_url = intent.getStringExtra("videoUrl").toString()
        caption = intent.getStringExtra("title").toString()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        player = findViewById(R.id.player)
        player.getToolbar().title = caption
        player.getToolbar().setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        player.getToolbar().setNavigationOnClickListener { onBackPressed() }
        getDrawable(R.drawable.play_icon)?.let {
            player.setButtonDrawable(
                BetterVideoPlayer.ButtonType.PlayButton,
                it
            )
        }
        getDrawable(R.drawable.pause_icon)?.let {
            player.setButtonDrawable(
                BetterVideoPlayer.ButtonType.PauseButton,
                it
            )
        }

        player.setSource(Uri.parse(BLOG_IMAGE_BASE_URL + video_url))
    }

    override fun onPause() {
        super.onPause()
        // Make sure the player stops playing if the user presses the home button.
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        player.clearAnimation()
    }

}

