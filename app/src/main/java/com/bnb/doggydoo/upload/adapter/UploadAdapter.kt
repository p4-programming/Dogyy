package com.bnb.doggydoo.upload.adapter


import android.content.Context
import androidx.fragment.app.*
import com.bnb.doggydoo.upload.ui.UploadArticleFragment
import com.bnb.doggydoo.upload.ui.UploadPhotoFragment

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