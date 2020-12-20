package com.ordolabs.chessmate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.ui.activity.NewGameActivity
import com.ordolabs.chessmate.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home_tab_game.*

class HomeGameTabFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = R.layout.fragment_home_tab_game
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setNewGameFAB()
    }

    private fun setNewGameFAB() {
        fab_new_game.setOnClickListener {
            startActivity(NewGameActivity.getStartingIntent(requireContext()))
        }
    }
}