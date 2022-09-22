package com.daon.goj_gam.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RestaurantDetailListFragmentPagerAdapter(
    activity: FragmentActivity,
    val fragmentList: List<Fragment>
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}