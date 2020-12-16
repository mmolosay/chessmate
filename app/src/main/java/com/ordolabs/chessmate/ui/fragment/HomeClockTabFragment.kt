package com.ordolabs.chessmate.ui.fragment

import android.animation.*
import android.content.Context
import android.graphics.*
import android.graphics.drawable.TransitionDrawable
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
import com.ordolabs.chessmate.util.Utils
import com.ordolabs.chessmate.util.struct.Timer
import com.ordolabs.chessmate.util.wrapper.ValueAnimatorBuilder
import com.ordolabs.chessmate.viewmodel.TimerSettingsViewModel
import com.ordolabs.chessmate.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_home_tab_clock.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeClockTabFragment : BaseFragment() {

    private val timerVM: TimerViewModel by viewModel()
    private val timerSettingsVM: TimerSettingsViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeTimerData()
        observeTimerState()
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
        setSettingsButton()
        setRestartButton()
        setStartStopButton()
        setPauseResumeButton()
    }

    private fun setTimer() {
        timer.isEnabled = false
    }

    private fun setSettingsButton() {
        btn_settings.isEnabled = false
        btn_settings.setOnClickListener {
            val settings = timerSettingsVM.settings.value ?: return@setOnClickListener
            TimerSettingsDialog
                .new(settings, ::onTimerSettingsDialogApplied)
                .show(parentFragmentManager, "timer_settings_dialog")
        }
    }

    private fun setRestartButton() {
        alterResetButtonEnabled(false)
        btn_restart.setOnClickListener {
            timerVM.addTimerCheckpoint()
            timerVM.restartTimer()
            if (timerVM.isTimerExpired()) {
                Utils.vibrate(context)
            }
        }
    }

    private fun setStartStopButton() {
        btn_startstop.setOnClickListener {
            val stopped = timerVM.isTimerStopped

            if (!stopped) {
                timerVM.addTimerCheckpoint()
                timerVM.stopTimer()
            } else {
                timerVM.clearTimerCheckpoints()
                timerVM.startTimer()
            }

            alterResetButtonEnabled(stopped)
            btn_settings.isEnabled = !stopped
            animTimerControlsTranslation(stopped)
            alterStartStopButtonIcon(!stopped)
        }
    }

    private fun setPauseResumeButton() {
        btn_pauseresume.setOnClickListener {
            val paused = timerVM.isTimerPaused

            if (paused) {
                timerVM.resumeTimer()
            } else { // running
                timerVM.pauseTimer()
            }

            alterPauseResumeButtonIcon(paused)
        }
    }

    private fun alterStartStopButtonIcon(setStart: Boolean) {
        val iconRes = if (setStart)
            R.drawable.ic_start_normal
        else
            R.drawable.ic_stop_normal
        val icon = ContextCompat.getDrawable(requireContext(), iconRes)
        btn_startstop.setImageDrawable(icon)
    }

    private fun alterPauseResumeButtonIcon(setPause: Boolean) {
        val iconRes = if (setPause)
            R.drawable.ic_pause_normal
        else
            R.drawable.ic_start_normal
        val icon = ContextCompat.getDrawable(requireContext(), iconRes)
        btn_pauseresume.setImageDrawable(icon)
    }

    private fun alterResetButtonEnabled(enabled: Boolean) {
        if (btn_restart.isEnabled == enabled) return
        btn_restart.isEnabled = enabled
        (btn_restart.background as TransitionDrawable).apply {
            if (!enabled) startTransition(200)
            else reverseTransition(200)
        }
    }

    private fun onTimerSettingsDialogApplied(newSettings: TimerSettingsPresentation) {
        timerSettingsVM.setTimerSettings(newSettings)
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

    private fun observeTimerState() {
        var previous = timerVM.timerState.value!!
        timerVM.timerState.observe(this) { state ->
            when (previous) {
                Timer.State.STOPPED -> when (state) {
                    Timer.State.RUNNING -> Unit
                    Timer.State.PAUSED -> Unit
                }
                Timer.State.PAUSED -> when (state) {
                    Timer.State.RUNNING -> Unit
                    Timer.State.STOPPED -> Unit
                }
                Timer.State.RUNNING -> when (state) {
                    Timer.State.PAUSED -> Unit
                    Timer.State.STOPPED -> Unit
                }
            }

            previous = state
        }
    }

    private fun observeTimerSettings() {
        timerSettingsVM.getTimerSettings().observe(this) {
            val limit = timerSettingsVM.parseTimerSettingsLimit(it)
            val names = it.player1 to it.player2

            timerVM.setTimerLimit(limit)
            timerVM.applyTimerLimit(limit)
            timerVM.setPlayerNames(names)

            timer.isEnabled = true
            btn_settings.isEnabled = true
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

    private fun animTimerControlsTranslation(running: Boolean) {
        val set = AnimatorSet()
        set.playTogether(
            animStartStopButtonTranslation(running),
            animPauseResumeButtonTranslation(running)
        )
        set.start()
    }

    private fun animStartStopButtonTranslation(isForward: Boolean) =
        ValueAnimatorBuilder.of<Float>(isForward) {
            values {
                val normal = resources
                    .getDimension(R.dimen.translationX_btn_timer_startstop_normal)
                val moved = resources
                    .getDimension(R.dimen.translationX_btn_timer_startstop_moved)
                arrayOf(normal, moved)
            }
            updateListener {
                btn_startstop.translationX = animatedValue as Float
            }
        }

    private fun animPauseResumeButtonTranslation(isForward: Boolean) =
        ValueAnimatorBuilder.of<Float>(isForward) {
            val resources = requireContext().resources
            values {
                val normal = resources
                    .getDimension(R.dimen.translationX_btn_timer_pauseresume_normal)
                val moved = resources
                    .getDimension(R.dimen.translationX_btn_timer_pauseresume_moved)
                arrayOf(normal, moved)
            }
            updateListener {
                btn_pauseresume.translationX = animatedValue as Float
                btn_pauseresume.alpha =
                    animatedFraction.takeIf { isForward } ?: 1 - animatedFraction
            }
        }.apply {
            doOnStart {
                if (isForward) {
                    btn_pauseresume.isVisible = true
                }
            }
            doOnEnd {
                if (!isForward) {
                    btn_pauseresume.isVisible = false
                    alterPauseResumeButtonIcon(!timerVM.isTimerPaused)
                }
            }
        }
}