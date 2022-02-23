package com.aks.doggydoo.newsfeed.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class NewsFeedDashboardAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return UploadedDataFragment()
            1 -> return ImageFrag()
            2 -> return ArticleFrag()
        }
        return UploadedDataFragment()
    }
}