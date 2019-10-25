package com.polairapp.polair

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SplashViewPagerAdapter(manager: FragmentManager, private var descriptions : ArrayList<Fragment>) : FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){


    override fun getItem(position: Int): Fragment {
        return descriptions[position]
    }

    override fun getCount(): Int {
        return descriptions.size
    }

}