package com.ordolabs.chessmate.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.chessmate.ui.dialog.TimerSettingsDialog
import com.ordolabs.chessmate.viewmodel.TimerSettingsViewModel
import com.ordolabs.chessmate.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_home_tab_clock.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeClockTabFragment private constructor() : Fragment() {

    private val timerVM: TimerViewModel by viewModel()
    private val timerSettingsVM: TimerSettingsViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeTimerTime()
        observeTimerSettings()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = R.layout.fragment_home_tab_clock
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setResetButton()
        setStartStopButton()
        setSettingsButton()
        setCheckpointsButton()
    }

    private fun setResetButton() {
        btn_reset_timer.isEnabled = false
        btn_reset_timer.setOnClickListener {
            timerVM.resetTimer()
        }
    }

    private fun setStartStopButton() {
        btn_startstop.setOnClickListener {
            val running = timerVM.isTimerRunning()
            if (running) {
                timerVM.stopTimer()
                timer.text = TimerViewModel.TIMER_UI_PATTERN
            } else {
                timerVM.startTimer()
            }
            btn_reset_timer.isEnabled = !running
            btn_settings.isEnabled = running
            alterStartStopButtonIcon(running)
        }
    }

    private fun setSettingsButton() {
        btn_settings.setOnClickListener {
            showTimerSettingsDialog()
        }
    }

    private fun setCheckpointsButton() {

    }

    private fun alterStartStopButtonIcon(setStart: Boolean) {
        val iconRes = if (setStart)
            R.drawable.ic_start_normal
        else
            R.drawable.ic_stop_normal
        val icon = ContextCompat.getDrawable(requireContext(), iconRes)
        btn_startstop.setImageDrawable(icon)
    }

    private fun showTimerSettingsDialog() {
        val settings = timerSettingsVM.settings.value ?: return
        TimerSettingsDialog.new(settings, ::onTimerSettingsDialogApplied)
            .show(parentFragmentManager, "stopwatch_settings_dialog")
    }

    private fun onTimerSettingsDialogApplied(newSettings: TimerSettingsPresentation) {
        timerSettingsVM.setTimerSettings(newSettings)

        val newLimit = timerSettingsVM.parseTimerSettingsLimit(newSettings)
        timerVM.setTimerLimit(newLimit)
    }

    private fun observeTimerTime() {
        timerVM.timerTime.observe(this) {
            timer.text = it.time
        }
    }

    private fun observeTimerSettings() {
        timerSettingsVM.getTimerSettings().observe(this) {
            val limit = timerSettingsVM.parseTimerSettingsLimit(it)
            timerVM.setTimerLimit(limit)
        }
    }

    companion object {
        fun new(): HomeClockTabFragment {
            return HomeClockTabFragment()
        }
    }
}