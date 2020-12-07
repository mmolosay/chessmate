package com.ordolabs.chessmate.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.ordolabs.chessmate.ui.fragment.HomeClockTabFragment
import com.ordolabs.chessmate.ui.fragment.HomeGameTabFragment

class HomeTabsPagerAdapter(
    fm: FragmentManager,
    lc: Lifecycle
) : FragmentStateAdapter(fm, lc) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> HomeClockTabFragment.new()
        1 -> HomeGameTabFragment.new()
        else -> throw IllegalArgumentException("position $position is unresolved")
    }
}