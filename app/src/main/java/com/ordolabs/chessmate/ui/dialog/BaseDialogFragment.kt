package com.ordolabs.chessmate.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import com.ordolabs.chessmate.R

abstract class BaseDialogFragment : DialogFragment() {

    @LayoutRes
    protected abstract fun getDialogLayoutRes(): Int

    @StyleRes
    protected open fun getDialogStyleRes(): Int {
        return R.style.DialogThemeWide
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, getDialogStyleRes())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = getDialogLayoutRes()
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) { }
}