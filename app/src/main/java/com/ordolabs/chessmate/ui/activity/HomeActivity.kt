package com.ordolabs.chessmate.ui.activity

import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.ui.activity.base.BaseActivity
import com.ordolabs.chessmate.ui.adapter.HomeTabsPagerAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(R.layout.activity_home) {

    override fun setViews() {
        setTabsPager()
        setTabLayout()
    }

    private fun setTabsPager() {
        val adapter = HomeTabsPagerAdapter(supportFragmentManager, lifecycle)
        home_pager.adapter = adapter
    }

    private fun setTabLayout() {
        TabLayoutMediator(home_tabs, home_pager, ::configureTab).attach()
    }

    private fun configureTab(tab: TabLayout.Tab, pos: Int) {
        val iconId: Int = when (pos) {
            0 -> R.drawable.ic_clock
            1 -> R.drawable.ic_game
            else -> throw IllegalArgumentException("pos $pos is unresolved")
        }
        val icon = ContextCompat.getDrawable(this, iconId)
        tab.icon = icon

        if (Build.VERSION.SDK_INT >= 23) {
            val tabRipple = ContextCompat.getDrawable(this, R.drawable.ripple_rounded_gray205)
            tab.view.background = tabRipple
        }
    }
}