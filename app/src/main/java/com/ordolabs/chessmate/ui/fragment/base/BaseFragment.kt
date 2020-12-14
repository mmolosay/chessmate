package com.ordolabs.chessmate.ui.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModels()
    }

    /**
     * Configures `Fragment`'s ViewModels.
     */
    protected open fun setViewModels() {
        // override me
    }
}