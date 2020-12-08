package com.ordolabs.chessmate.ui.dialog

import android.os.Bundle
import android.view.View
import com.ordolabs.chessmate.R
import kotlinx.android.synthetic.main.dialog_stopwatch_settings.view.*

class StopwatchSettingsDialog : BaseDialogFragment() {

    override fun getDialogLayoutRes(): Int {
        return R.layout.dialog_stopwatch_settings
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        setOkButton(root)
    }

    private fun setOkButton(root: View) {
        root.d_stopwatch_settings_btn_ok.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun new(): StopwatchSettingsDialog {
            return StopwatchSettingsDialog()
        }
    }
}