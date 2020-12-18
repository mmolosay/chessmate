package com.ordolabs.chessmate.ui.fragment

import android.animation.*
import android.content.Context
import android.graphics.*
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.mapper.toUI
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.chessmate.ui.adapter.CheckpointsAdapter
import com.ordolabs.chessmate.ui.adapter.base.OnRecyclerItemClicksListener
import com.ordolabs.chessmate.ui.dialog.TimerSettingsDialog
import com.ordolabs.chessmate.ui.fragment.base.BaseFragment
import com.ordolabs.chessmate.util.Utils
import com.ordolabs.chessmate.util.wrapper.ValueAnimatorBuilder
import com.ordolabs.chessmate.viewmodel.TimerSettingsViewModel
import com.ordolabs.chessmate.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_home_tab_clock.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeClockTabFragment : BaseFragment() {

    private val timerVM: TimerViewModel by viewModel()
    private val timerSettingsVM: TimerSettingsViewModel by viewModel()

    private val checkpointsAdapter by lazy {
        CheckpointsAdapter(object : OnRecyclerItemClicksListener {})
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeTimerData()
        observeTimerState()
        observeTimerSettings()
        observeTimerCheckpoints()
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
        setCheckpointsRecycler()
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
        alterRestartButtonEnabled(false)
        btn_restart.setOnClickListener {
            timerVM.addTimerCheckpoint()
            timerVM.restartTimer()

            animRestartButtonClick()
            alterPauseResumeButtonIcon(true)
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
                checkpointsAdapter.clear()
                timerVM.clearTimerCheckpoints()
                timerVM.startTimer()
            }

            animTimerViews(stopped)
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

    private fun setCheckpointsRecycler() {
        val reverseLayout = true
        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, reverseLayout)

        rv_checkpoints.layoutManager = lm
        rv_checkpoints.adapter = checkpointsAdapter
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

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun observeTimerState() {
        timerVM.timerState.observe(this) {

        }
    }

    private fun observeTimerSettings() {
        timerSettingsVM.getTimerSettings().observe(this) {
            val limit = timerSettingsVM.parseTimerSettingsLimit(it)

            timerVM.setTimerLimit(limit)
            timerVM.applyTimerLimit(limit)

            timer.isEnabled = true
            btn_settings.isEnabled = true
        }
    }

    private fun observeTimerCheckpoints() {
        timerVM.timerCheckpoints.observe(this) { checkpoint ->
            checkpoint ?: return@observe
            checkpointsAdapter.add(
                checkpoint.toUI(requireContext(), timerSettingsVM.settings.value!!)
            )
        }
    }

    private fun observeTimerWarnViewState() {
        timerVM.warnState.observe(this) { state ->
            if (timer_warn.state != state) {
                timer_warn.setState(state)
            }
        }
    }


    private fun animTimerViews(stopped: Boolean) {
        btn_settings.isEnabled = !stopped

        animTimerControls(stopped)
        alterStartStopButtonIcon(!stopped)

        animCheckpointsDividerScaleX()
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

    // will toggle visibility instantly, but animator of btn_restart would last
    // R.integer.anim_btn_round_large_scale_disabled milliseconds,
    // so we should return empty animator with that exact duration
    private fun animRestartButtonEnabled(enabled: Boolean): ValueAnimator {
        val duration = resources.getInteger(R.integer.anim_btn_round_large_scale)
        return ValueAnimator.ofInt(0, 1000).apply {
            this.duration = duration.toLong()
            doOnStart {
                alterRestartButtonEnabled(enabled)
                if (enabled) btn_restart.isInvisible = !enabled
            }
            doOnEnd {
                if (!enabled) btn_restart.isInvisible = !enabled
            }
        }
    }

    private fun animRestartButtonClick() {
        animRestartButtonRotation().apply {
            doOnEnd {
                btn_restart.rotation = 0f
            }
        }.start()
    }

    private fun animRestartButtonRotation() =
        ValueAnimatorBuilder.of<Float>(true) {
            values {
                arrayOf(0f, 180f)
            }
            interpolator {
                OvershootInterpolator()
            }
            duration {
                resources.getInteger(R.integer.anim_duration_500).toLong()
            }
            updateListener {
                btn_restart.rotation = animatedValue as Float
            }
        }

    private fun animTimerControls(running: Boolean) {
        val set = AnimatorSet()
        // reverse order
        if (running) set.apply {
            playSequentially(
                animTimerControlsTranslation(running),
                animRestartButtonEnabled(running)
            )
            doOnStart {
                alterPauseResumeButtonIcon(running)
            }
        }
        else set.apply {
            playSequentially(
                animRestartButtonEnabled(running),
                animTimerControlsTranslation(running)
            )
            doOnEnd {
                alterPauseResumeButtonIcon(running)
            }
        }
        set.start()
    }

    private fun animTimerControlsTranslation(running: Boolean): AnimatorSet {
        val set = AnimatorSet()
        set.playTogether(
            animStartStopButtonTranslation(running),
            animPauseResumeButtonTranslation(running)
        )
        return set
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
            duration {
                resources.getInteger(R.integer.anim_timer_controls).toLong()
            }
            updateListener {
                btn_startstop.translationX = animatedValue as Float
            }
        }

    private fun animPauseResumeButtonTranslation(isForward: Boolean) =
        ValueAnimatorBuilder.of<Float>(isForward) {
            values {
                val normal = resources
                    .getDimension(R.dimen.translationX_btn_timer_pauseresume_normal)
                val moved = resources
                    .getDimension(R.dimen.translationX_btn_timer_pauseresume_moved)
                arrayOf(normal, moved)
            }
            duration {
                resources.getInteger(R.integer.anim_timer_controls).toLong()
            }
            updateListener {
                btn_pauseresume.translationX = animatedValue as Float
                btn_pauseresume.alpha =
                    animatedFraction.takeIf { isForward } ?: 1 - animatedFraction
            }
        }.apply {
            doOnStart {
                if (isForward) btn_pauseresume.isVisible = true
            }
            doOnEnd {
                if (!isForward) btn_pauseresume.isVisible = false
            }
        }

    private fun animCheckpointsDividerScaleX() {
        // anim divider appearing only at first timer start
        if (divider.scaleX == 1f) return
        ValueAnimatorBuilder.of<Float>(true) {
            values {
                arrayOf(0f, 1f)
            }
            updateListener {
                divider.scaleX = animatedValue as Float
            }
        }.start()
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

    private fun alterRestartButtonEnabled(enabled: Boolean) {
        if (btn_restart.isEnabled == enabled) return
        btn_restart.isEnabled = enabled
        (btn_restart.background as TransitionDrawable).apply {
            if (!enabled) startTransition(200)
            else reverseTransition(200)
        }
    }
}