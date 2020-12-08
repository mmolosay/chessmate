package com.ordolabs.chessmate.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ordolabs.chessmate.R
import kotlinx.android.synthetic.main.dialog_stopwatch_settings.view.*

class StopwatchSettingsDialog private constructor(context: Context) : DialogFragment() {

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = R.layout.dialog_stopwatch_settings
        root = inflater.inflate(layout, container, false)
        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window ?: return dialog
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        root.d_stopwatch_settings_btn_ok.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogThemeWide)
    }

    companion object {
        fun new(context: Context): StopwatchSettingsDialog {
            return StopwatchSettingsDialog(context)
        }
    }
}