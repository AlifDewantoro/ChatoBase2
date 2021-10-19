package com.gamatechno.chato.sdk.app.sharedmedia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedfile.SharedFileFragment
import com.gamatechno.chato.sdk.app.sharedmedia.fragment.sharedimage.SharedMediaFragment

class SharedMediaFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){
    override fun getItem(p0: Int): Fragment {
        if(p0.equals(0))
            return SharedMediaFragment()
        else
            return SharedFileFragment()
    }

    override fun getCount(): Int {
        return 2;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position.equals(0))
            return "Media"
        else
            return "Dokumen"
    }

}