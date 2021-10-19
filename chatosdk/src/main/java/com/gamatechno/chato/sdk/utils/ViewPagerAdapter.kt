package com.gamatechno.chato.sdk.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : SmartFragmentStatePagerAdapter(fm, behavior) {
    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    fun addFragments(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}