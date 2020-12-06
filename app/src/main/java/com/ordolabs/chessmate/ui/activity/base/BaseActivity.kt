package com.ordolabs.chessmate.ui.activity.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        setUp()
        setViews()
    }

    /**
     * Configures non-view components.
     */
    @CallSuper
    protected open fun setUp() {
        parseStartIntent()
    }

    /**
     * Parses `Intent`, that started this `Activity`.
     */
    protected open fun parseStartIntent() {
        // override me
    }

    /**
     * Sets activity's views and configures them.
     */
    protected open fun setViews() {
        // override me
    }

    companion object {
        // extra keys and stuff
    }
}