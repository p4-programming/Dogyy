package com.bnb.doggydoo.article.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.loadImageAsGif
import com.bnb.doggydoo.databinding.FragArticleIntroBinding
import com.bnb.doggydoo.utils.CommonMethod

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
        CommonMethod.makeTransparentStatusBar(activity?.window)
        binding.articleGif.loadImageAsGif(requireContext(), R.raw.article_gif)
    }
}