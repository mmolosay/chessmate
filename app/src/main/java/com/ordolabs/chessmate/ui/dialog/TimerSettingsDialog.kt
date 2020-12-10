package com.ordolabs.chessmate.ui.dialog

import android.os.Bundle
import android.view.View
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.TimerSettingsPresentation
import com.ordolabs.chessmate.viewmodel.TimerSettingsViewModel
import kotlinx.android.synthetic.main.dialog_timer_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimerSettingsDialog(
    private val onOkClicked: (TimerSettingsPresentation) -> Unit
) : BaseDialogFragment() {

    private val timerSettingsVM: TimerSettingsViewModel by viewModel()

    private val editLimitMinutes by lazy { edit_timer_limit_minutes }
    private val editLimitSeconds by lazy { d_timer_settings_edit_limit_seconds }
    private val editPlayer1 by lazy { edit_player1 }
    private val editPlayer2 by lazy { edit_player2 }
    private val btnSwapPlayers by lazy { btn_swap_players }
    private val btnOk by lazy { btn_ok }

    override fun getDialogLayoutRes(): Int {
        return R.layout.dialog_timer_settings
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        observeSavedSettings()
        setSwapPlayersButton()
        setOkButton()
    }

    private fun observeSavedSettings() {
        timerSettingsVM.getTimerSettings().observe(this) { settings ->
            editLimitMinutes.setText(settings.limitMinutes.toString())
            editLimitSeconds.setText(settings.limitSeconds.toString())
            editPlayer1.setText(settings.player1)
            editPlayer2.setText(settings.player2)
        }
    }

    private fun setSwapPlayersButton() {
        btnSwapPlayers.setOnClickListener {
            val player1 = editPlayer1.text
            editPlayer1.text = editPlayer2.text
            editPlayer2.text = player1
        }
    }

    private fun setOkButton() {
        btnOk.setOnClickListener {
            val settings = collectSettings()
            timerSettingsVM.setTimerSettings(settings)
            onOkClicked(settings)
            dismiss()
        }
    }

    private fun collectSettings(): TimerSettingsPresentation {
        val minutes = editLimitMinutes.text.toString().toInt()
        val seconds = editLimitSeconds.text.toString().toInt()
        val player1 = editPlayer1.text.toString()
        val player2 = editPlayer2.text.toString()
        return TimerSettingsPresentation(minutes, seconds, player1, player2)
    }

    companion object {
        fun new(
            onOkClicked: (TimerSettingsPresentation) -> Unit
        ): TimerSettingsDialog {
            return TimerSettingsDialog(onOkClicked)
        }
    }
}