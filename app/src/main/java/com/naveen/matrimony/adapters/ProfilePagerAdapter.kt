package com.naveen.matrimony.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.naveen.matrimony.views.main.ProfileFragment

class ProfilePagerAdapter(var fm: FragmentActivity, var list: ArrayList<String>?) :
    FragmentStateAdapter(fm) {


    override fun getItemCount(): Int {
        return list?.size!!

    }

    override fun createFragment(position: Int): Fragment =
        ProfileFragment.newInstance(list!![position], list!!.size, position)

}