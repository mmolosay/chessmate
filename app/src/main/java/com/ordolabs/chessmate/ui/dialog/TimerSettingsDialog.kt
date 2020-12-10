package com.ordolabs.chessmate.ui.dialog

import android.os.Bundle
import android.view.View
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import kotlinx.android.synthetic.main.dialog_timer_settings.*

class TimerSettingsDialog(
    private val settings: TimerSettingsPresentation,
    private val onDialogApplied: (TimerSettingsPresentation) -> Unit
) : BaseDialogFragment() {

    override fun getDialogLayoutRes(): Int {
        return R.layout.dialog_timer_settings
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        setSettingsInViews()
        setSwapPlayersButton()
        setOkButton()
    }

    private fun setSettingsInViews() {
        edit_timer_limit_minutes.setText(settings.limitMinutes.toString())
        edit_timer_limit_seconds.setText(settings.limitSeconds.toString())
        edit_player1.setText(settings.player1)
        edit_player2.setText(settings.player2)
    }

    private fun setSwapPlayersButton() {
        btn_swap_players.setOnClickListener {
            val player1 = edit_player1.text
            edit_player1.text = edit_player2.text
            edit_player2.text = player1
        }
    }

    private fun setOkButton() {
        btn_apply.setOnClickListener {
            val settings = collectSettings()
            onDialogApplied(settings)
            dismiss()
        }
    }

    private fun collectSettings(): TimerSettingsPresentation {
        val minutes = edit_timer_limit_minutes.text.toString().toInt()
        val seconds = edit_timer_limit_seconds.text.toString().toInt()
        val player1 = edit_player1.text.toString()
        val player2 = edit_player2.text.toString()
        return TimerSettingsPresentation(minutes, seconds, player1, player2)
    }

    companion object {
        fun new(
            settings: TimerSettingsPresentation,
            onApplied: (TimerSettingsPresentation) -> Unit
        ): TimerSettingsDialog {
            return TimerSettingsDialog(settings, onApplied)
        }
    }
}