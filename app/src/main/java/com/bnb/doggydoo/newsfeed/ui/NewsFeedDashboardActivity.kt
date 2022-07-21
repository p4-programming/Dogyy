package com.bnb.doggydoo.newsfeed.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.databinding.ActivityNewsfeedDeshboardBinding
import com.bnb.doggydoo.upload.ui.UploadActivity
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MyApp
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

val titles = arrayOf(
    "ALL",
    "MEDIA",
    "ARTICLES"
)
@AndroidEntryPoint
class NewsFeedDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsfeedDeshboardBinding
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    private var fromValue:String =""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsfeedDeshboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonMethod.makeTransparentStatusBar(window)
        getInit()
        setViewPager()
    }

    private fun getInit() {
        binding.ivback.setOnClickListener {
            finish()
        }

        if (!MyApp.getSharedPref().newsTypeFilter.isEmpty()){
            binding.friends.text = MyApp.getSharedPref().newsTypeFilter
        }else{
            binding.friends.text = "All"
            MyApp.getSharedPref().newsTypeFilter = "All"
        }

        binding.friends.setOnClickListener {
            blockDialog()
          /*  OptionBS() {
                binding.friends.text = it
            }.show(supportFragmentManager, binding.friends.text.toString())*/
        }
        binding.ivUpload.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        binding.searchview.setOnClickListener {
            binding.searchview.onActionViewExpanded()
            binding.searchText.hide()

        }
        binding.searchview.setOnSearchClickListener {
            binding.searchText.hide()
        }
        binding.searchview.setOnCloseListener {
            binding.searchview.onActionViewCollapsed()
            binding.searchText.show()
            true
        }

        if (MyApp.getSharedPref().newsTypeFilter.toString().isEmpty()){
            MyApp.getSharedPref().newsTypeFilter = "All"
            binding.friends.text = "All"
        }else{
            binding.friends.text = MyApp.getSharedPref().newsTypeFilter.toString()
        }
    }

    private fun setViewPager() {
        val viewPager = binding.tabViewpager
        val tabLayout = binding.tabTablayout

        val adapter = NewsFeedDashboardAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        viewPager.currentItem = 0

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun blockDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_newfeedview)

        val tvAll = dialog.findViewById<View>(R.id.tvAll) as TextView
        val tvFriend = dialog.findViewById<View>(R.id.tvFriend) as TextView
        val tvMyPost = dialog.findViewById<View>(R.id.tvMyPost) as TextView
        val ivCancel = dialog.findViewById<View>(R.id.ivCancel) as ImageView

        tvAll.setOnClickListener {
            MyApp.getSharedPref().newsTypeFilter = "All"
            tvAll.setTextColor(Color.parseColor("#5935D5"))
            startActivity(Intent(this, NewsFeedDashboardActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            dialog.dismiss()
        }

        tvFriend.setOnClickListener {
            MyApp.getSharedPref().newsTypeFilter = "My Friends"
            tvFriend.setTextColor(Color.parseColor("#5935D5"))
            startActivity(Intent(this, NewsFeedDashboardActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            dialog.dismiss()
        }

        tvMyPost.setOnClickListener {
            MyApp.getSharedPref().newsTypeFilter = "My Post"
            tvFriend.setTextColor(Color.parseColor("#5935D5"))
            startActivity(Intent(this, NewsFeedDashboardActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            dialog.dismiss()
        }

        ivCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}