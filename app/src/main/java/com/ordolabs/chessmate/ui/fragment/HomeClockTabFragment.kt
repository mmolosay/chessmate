package com.ordolabs.chessmate.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.ui.dialog.StopwatchSettingsDialog
import com.ordolabs.chessmate.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home_tab_clock.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeClockTabFragment private constructor() : Fragment() {

    private val homeVM: HomeViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeStopwatchTime()
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
        tab_clock_btn_reset.isEnabled = false
        tab_clock_btn_reset.setOnClickListener {
            homeVM.resetStopwatch()
        }
    }

    private fun setStartStopButton() {
        tab_clock_btn_startstop.setOnClickListener {
            val running = homeVM.isStopwatchRunning()
            if (running) {
                homeVM.stopStopwatch()
                tab_clock_stopwatch.text = HomeViewModel.STOPWATCH_PATTERN
            } else {
                homeVM.startStopwatch()
            }
            tab_clock_btn_reset.isEnabled = !running
            alterStartStopButtonIcon(running)
        }
    }

    private fun setSettingsButton() {
        tab_clock_btn_settings.setOnClickListener {
            StopwatchSettingsDialog
                .new()
                .show(parentFragmentManager, "stopwatch_settings_dialog")
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
        tab_clock_btn_startstop.setImageDrawable(icon)
    }

    private fun observeStopwatchTime() {
        homeVM.stopwatchTime.observe(this) { time ->
            tab_clock_stopwatch.text = time
        }
    }

    companion object {
        fun new(): HomeClockTabFragment {
            return HomeClockTabFragment()
        }
    }
}