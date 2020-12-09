package com.ordolabs.chessmate.ui.dialog

import android.os.Bundle
import android.view.View
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.StopwatchSettingsPresentation
import com.ordolabs.chessmate.viewmodel.StopwatchSettingsViewModel
import kotlinx.android.synthetic.main.dialog_stopwatch_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StopwatchSettingsDialog(
    private val onOkClicked: (StopwatchSettingsPresentation) -> Unit
) : BaseDialogFragment() {

    private val stopwatchSettingsVM: StopwatchSettingsViewModel by viewModel()

    private val editLimitMinutes by lazy { d_stopwatch_settings_edit_limit_minutes }
    private val editLimitSeconds by lazy { d_stopwatch_settings_edit_limit_seconds }
    private val editPlayer1 by lazy { d_stopwatch_settings_edit_player1 }
    private val editPlayer2 by lazy { d_stopwatch_settings_edit_player2 }
    private val btnSwapPlayers by lazy { d_stopwatch_settings_players_btn_swap }
    private val btnOk by lazy { d_stopwatch_settings_btn_ok }

    override fun getDialogLayoutRes(): Int {
        return R.layout.dialog_stopwatch_settings
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        observeSavedSettings()
        setSwapPlayersButton()
        setOkButton()
    }

    private fun observeSavedSettings() {
        stopwatchSettingsVM.getStopwatchSettings().observe(this) { settings ->
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
            stopwatchSettingsVM.setStopwatchSettings(settings)
            onOkClicked(settings)
            dismiss()
        }
    }

    private fun collectSettings(): StopwatchSettingsPresentation {
        val minutes = editLimitMinutes.text.toString().toInt()
        val seconds = editLimitSeconds.text.toString().toInt()
        val player1 = editPlayer1.text.toString()
        val player2 = editPlayer2.text.toString()
        return StopwatchSettingsPresentation(minutes, seconds, player1, player2)
    }

    companion object {
        fun new(
            onOkClicked: (StopwatchSettingsPresentation) -> Unit
        ): StopwatchSettingsDialog {
            return StopwatchSettingsDialog(onOkClicked)
        }
    }
}