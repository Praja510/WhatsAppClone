package com.mprajadinata.whatsappclone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mprajadinata.whatsappclone.MainActivity

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {

        return MainActivity.PlaceHolderFragment.newIntent(
            position + 1
        )
    }

    override fun getCount(): Int {

        return 3
    }
}