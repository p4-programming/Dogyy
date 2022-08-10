package com.bnb.doggydoo.sos.ui

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.bnb.doggydoo.databinding.ActivityAdoptionIntroBinding
import com.bnb.doggydoo.databinding.ActivitySosIntroBinding
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.ViewPager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SOSIntro : AppCompatActivity() {
    private lateinit var binding: ActivitySosIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosIntroBinding.inflate(layoutInflater)
        CommonMethod.makeTransparentStatusBar(window)

//        val fm: FragmentManager = supportFragmentManager
//        val fragment = SOSDistressFragment()
//        fm.beginTransaction().replace(R.id.,fragment).commit()
//       // fm.beginTransaction().add(SOSIntro::class.java,fragment).commit()


        setContentView(binding.root)
        val viewPager = ViewPager(supportFragmentManager)
        viewPager.addFragment(SOSIntroFragment())
        viewPager.addFragment(SOSDistressFragment())
       // setContentView(binding.pager)
        binding.pager.adapter = viewPager
    }
}