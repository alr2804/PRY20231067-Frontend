package com.upc.pry20231067.ui.foro

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        if(position == 0){
            return ForoTab1Fragment()
        }else if (position == 1){
            return ForoTab2Fragment()
        } else {
            return ForoTab1Fragment()
        }
    }

}