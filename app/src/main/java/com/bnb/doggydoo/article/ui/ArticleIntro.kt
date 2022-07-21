package com.bnb.doggydoo.article.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnb.doggydoo.databinding.ActivityArticleIntroBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleIntro : AppCompatActivity() {
    private lateinit var binding: ActivityArticleIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CommonMethod.makeTransparentStatusBar(window)

        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(ArticleIntroFrag())
        viewPager.addFragment(ArticleMain())
        binding.pager.adapter = viewPager
    }
}