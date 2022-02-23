package com.aks.doggydoo.article.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.aks.doggydoo.article.adapter.ViewAllArticleAdapter
import com.aks.doggydoo.article.datasource.model.Articledetail
import com.aks.doggydoo.article.viewmodel.ArticleViewModel
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.commonutility.snack
import com.aks.doggydoo.databinding.ActivityArticleviewallBinding
import com.aks.doggydoo.utils.MyApp
import com.aks.doggydoo.utils.helper.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleviewallBinding
    private lateinit var adapter: ViewAllArticleAdapter
    var viewType: String = ""
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleviewallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callAllArticleAPI()
    }

    private fun getInit() {
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        viewType = intent.getStringExtra("viewType").toString()
        adapter = ViewAllArticleAdapter(this)
        binding.rvAllItems.layoutManager = GridLayoutManager(this, 2)
        binding.rvAllItems.adapter = adapter

        //Toast.makeText(this, viewType, Toast.LENGTH_SHORT).show()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun callAllArticleAPI() {
        articleViewModel.getAllArticleDataHome(MyApp.getSharedPref().userId,viewType)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        renderValues(it.data.allblogList)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun renderValues(allblogList: List<Articledetail>) {
        adapter.addData(allblogList)
    }

}