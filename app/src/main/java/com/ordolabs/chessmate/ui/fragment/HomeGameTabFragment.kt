package com.ordolabs.chessmate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.ui.fragment.base.BaseFragment

class HomeGameTabFragment private constructor() : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = R.layout.fragment_home_tab_game
        return inflater.inflate(layout, container, false)
    }

    companion object {
        fun new(): HomeGameTabFragment {
            return HomeGameTabFragment()
        }
    }
}