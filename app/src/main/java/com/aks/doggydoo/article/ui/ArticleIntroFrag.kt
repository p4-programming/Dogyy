package com.aks.doggydoo.article.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.loadImageAsGif
import com.aks.doggydoo.databinding.FragArticleIntroBinding

class ArticleIntroFrag : Fragment() {

    private lateinit var binding: FragArticleIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragArticleIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.articleGif.loadImageAsGif(requireContext(), R.raw.article_gif)
    }
}