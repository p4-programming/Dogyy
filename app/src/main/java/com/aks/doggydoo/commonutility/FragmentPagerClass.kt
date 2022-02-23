package com.aks.doggydoo.commonutility

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentPagerClass(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    var fragmentArrayList = ArrayList<Fragment>()
    var stringArrayList = ArrayList<String>()
    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    fun addFragment(fragment: Fragment, string: String) {
        fragmentArrayList.add(fragment)
        stringArrayList.add(string)
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return stringArrayList[position]
    }
}