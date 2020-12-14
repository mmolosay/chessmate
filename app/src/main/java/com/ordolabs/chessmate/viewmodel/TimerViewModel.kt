package com.ordolabs.chessmate.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ordolabs.chessmate.model.TimerData
import com.ordolabs.chessmate.ui.view.TimerWarnView
import com.ordolabs.chessmate.util.struct.Timer
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel
import kotlin.math.absoluteValue
import kotlin.math.sign

class TimerViewModel : BaseViewModel() {

    val timerData: LiveData<TimerData>
        get() = _timerData

    val warnState: LiveData<TimerWarnView.State>
        get() = _warnState

    private val _timerData = MutableLiveData(
        TimerData(TIMER_UI_PATTERN, 0, false)
    )
    private val _warnState = MutableLiveData(
        TimerWarnView.State.HIDDEN
    )

    private val timer = Timer()
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerTick = object : Runnable {
        override fun run() {
            val remaining = timer.getRemainingTime()
            updateTimerTime(remaining)
            updateWarnState(remaining)
            timerHandler.postDelayed(this, TIMER_UI_UPDATE_DELTA_TIME)
        }
    }

    fun isTimerRunning(): Boolean {
        return timer.running
    }

    fun isTimerExpired(): Boolean {
        val remaining = _timerData.value?.rawTime!!
        return (remaining < 0)
    }

    fun setTimerLimit(limit: Long) {
        timer.limit = limit
    }

    fun startTimer() {
        timer.start()
        timerHandler.post(timerTick)
    }

    fun stopTimer() {
        timer.stop()
        timerHandler.removeCallbacks(timerTick)

        val remaining = timer.limit
        updateTimerTime(remaining)
        updateWarnState(remaining)
    }

    fun resetTimer() {
        if (timer.running) {
            timer.restart()
        }
        _timerData.value?.time = TIMER_UI_PATTERN
        _timerData.value?.hasMinus = false
    }

    fun updateTimerTime(remaining: Long) {
        val r = remaining.absoluteValue

        val minutes = r / 1000 / 60
        val seconds = (r - (minutes * 1000 * 60)) / 1000
        val millis = (r - (minutes * 1000 * 60 + seconds * 1000)) / 10

        val min = if (minutes / 10 == 0L) "0$minutes" else minutes.toString()
        val sec = if (seconds / 10 == 0L) "0$seconds" else seconds.toString()
        val mil = if (millis / 10 == 0L) "0$millis" else millis.toString()

        _timerData.value?.apply {
            time = "$min:$sec.$mil"
            rawTime = remaining
            hasMinus = (remaining.sign == -1)
        }
        _timerData.value = _timerData.value // will fire observers
    }

    private fun updateWarnState(remaining: Long) {
        _warnState.value = when {
            remaining < 0 ->
                TimerWarnView.State.EXPANDED

            remaining < TimerWarnView.WARN_APPEAR_PREEMPTION ->
                TimerWarnView.State.COLLAPSED

            else ->
                TimerWarnView.State.HIDDEN
        }
    }

    companion object {
        private const val TIMER_UI_UPDATE_FREQ = 50L
        private const val TIMER_UI_UPDATE_DELTA_TIME = 1000 / TIMER_UI_UPDATE_FREQ

        const val TIMER_UI_PATTERN = "00:00.00"
    }
}