package com.aks.doggydoo.upload.adapter


import android.content.Context
import androidx.fragment.app.*
import com.aks.doggydoo.upload.ui.UploadArticleFragment
import com.aks.doggydoo.upload.ui.UploadPhotoFragment

class UploadAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UploadPhotoFragment()
            }
            1 -> {
                UploadArticleFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}