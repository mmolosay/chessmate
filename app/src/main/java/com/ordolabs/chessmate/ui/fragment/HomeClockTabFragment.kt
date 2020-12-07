package com.ordolabs.chessmate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ordolabs.chessmate.R

class HomeClockTabFragment private constructor() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = R.layout.fragment_home_tab_clock
        return inflater.inflate(layout, container, false)
    }

    companion object {
        fun new(): HomeClockTabFragment {
            return HomeClockTabFragment()
        }
    }
}