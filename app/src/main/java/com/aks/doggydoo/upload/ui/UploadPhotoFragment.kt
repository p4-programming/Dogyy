package com.aks.doggydoo.upload.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.aks.doggydoo.databinding.FragmentUploadPhotoBinding
import com.aks.doggydoo.newsfeed.ui.UploadPhotoActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UploadPhotoFragment : Fragment() {
    private var _binding: FragmentUploadPhotoBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUploadPhotoBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlPhoto.setOnClickListener {
            ChooseOption()
        }
        binding.rlVideo.setOnClickListener {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                this.type = "video/*"
                resultLauncher.launch(this)
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val uri = result.data?.data
                    startActivity(
                        Intent(requireActivity(), UploadPhotoActivity::class.java)
                            .putExtra("uri", uri.toString())
                            .putExtra("type", "video")
                    )
                }
            }
        }


    private fun ChooseOption() {
        ImagePicker.with(this)
            .crop()
            .compress(500)
            .maxResultSize(
                500,
                500
            )
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uri = data?.data!!
            startActivity(
                Intent(requireActivity(), UploadPhotoActivity::class.java)
                    .putExtra("uri", uri.toString())
                    .putExtra("type", "image")
            )
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}