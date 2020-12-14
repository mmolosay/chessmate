package com.ordolabs.chessmate.ui.fragment

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.chessmate.ui.dialog.TimerSettingsDialog
import com.ordolabs.chessmate.ui.fragment.base.BaseFragment
import com.ordolabs.chessmate.util.wrapper.ValueAnimatorBuilder
import com.ordolabs.chessmate.viewmodel.TimerSettingsViewModel
import com.ordolabs.chessmate.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_home_tab_clock.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeClockTabFragment private constructor() : BaseFragment() {

    private val timerVM: TimerViewModel by viewModel()
    private val timerSettingsVM: TimerSettingsViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeTimerData()
        observeTimerSettings()
        observeTimerWarnViewState()
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
        super.onViewCreated(view, savedInstanceState)
        setTimer()
        setResetButton()
        setStartStopButton()
        setSettingsButton()
        setCheckpointsButton()
    }

    private fun setTimer() {
        timer.isEnabled = false
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
            val settings = timerSettingsVM.settings.value ?: return@setOnClickListener
            TimerSettingsDialog
                .new(settings, ::onTimerSettingsDialogApplied)
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
        btn_startstop.setImageDrawable(icon)
    }

    private fun onTimerSettingsDialogApplied(newSettings: TimerSettingsPresentation) {
        timerSettingsVM.setTimerSettings(newSettings)

        val newLimit = timerSettingsVM.parseTimerSettingsLimit(newSettings)
        timerVM.setTimerLimit(newLimit)
    }

    private fun observeTimerData() {
        var hadMinus = false
        timerVM.timerData.observe(this) { data ->
            timer.text = data.time

            if (data.hasMinus && !hadMinus)
                animTimerMinusShow()
            if (!data.hasMinus && hadMinus)
                animTimerMinusHide()
            hadMinus = data.hasMinus
        }
    }

    private fun observeTimerSettings() {
        timerSettingsVM.getTimerSettings().observe(this) {
            val limit = timerSettingsVM.parseTimerSettingsLimit(it)
            timerVM.setTimerLimit(limit)
            timerVM.updateTimerTime(limit)
            timer.isEnabled = true
        }
    }

    private fun observeTimerWarnViewState() {
        timerVM.warnState.observe(this) { state ->
            if (timer_warn.state != state) {
                timer_warn.setState(state)
            }
        }
    }

    private fun animTimerMinusShow() {
        val set = AnimatorSet()
        set.playTogether(
            animTimerMinusTranslation(true),
            animTimerMinusAlpha(true)
        )
        set.doOnStart {
            timer_minus.isVisible = true
        }
        set.start()
    }

    private fun animTimerMinusHide() {
        val set = AnimatorSet()
        set.playTogether(
            animTimerMinusTranslation(false),
            animTimerMinusAlpha(false)
        )
        set.doOnEnd {
            timer_minus.isGone = true
        }
        set.start()
    }

    private fun animTimerMinusTranslation(isForward: Boolean) =
        ValueAnimatorBuilder.of<Float>(isForward) {
            looped { false }
            values {
                val translation = resources.getDimensionPixelSize(
                    R.dimen.translationX_timer_minus
                ).toFloat()
                if (isForward) {
                    arrayOf(-translation, 0f)
                } else {
                    arrayOf(0f, translation)
                }
            }
            updateListener {
                timer_minus.translationX = animatedValue as Float
            }
        }

    private fun animTimerMinusAlpha(isForward: Boolean) =
        ValueAnimatorBuilder.of<Float>(isForward) {
            values {
                arrayOf(0f, 1f)
            }
            updateListener {
                timer_minus.alpha = animatedValue as Float
            }
        }

    companion object {
        fun new(): HomeClockTabFragment {
            return HomeClockTabFragment()
        }
    }
}