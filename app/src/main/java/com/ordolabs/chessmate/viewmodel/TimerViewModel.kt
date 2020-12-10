package com.ordolabs.chessmate.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ordolabs.chessmate.model.TimerTime
import com.ordolabs.chessmate.util.struct.Timer
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel
import kotlin.math.absoluteValue
import kotlin.math.sign

class TimerViewModel : BaseViewModel() {

    val timerTime: LiveData<TimerTime>
        get() = _timerTime

    private val _timerTime = MutableLiveData(
        TimerTime(TIMER_UI_PATTERN, false)
    )

    private val timer = Timer()
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerTick = object : Runnable {
        override fun run() {
            val elapsed = timer.getRemainingTime()
            updateTimerTime(elapsed)
            timerHandler.postDelayed(this, TIMER_UI_UPDATE_DELTA_TIME)
        }
    }

    fun isTimerRunning(): Boolean {
        return timer.running
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
    }

    fun resetTimer() {
        if (timer.running) {
            timer.restart()
        }
        _timerTime.value?.time = TIMER_UI_PATTERN
        _timerTime.value?.hasMinus = false
    }

    private fun updateTimerTime(elapsed: Long) {
        val e = elapsed.absoluteValue

        val minutes = e / 1000 / 60
        val seconds = (e - (minutes * 1000 * 60)) / 1000
        val millis = (e - (minutes * 1000 * 60 + seconds * 1000)) / 10

        val min = if (minutes / 10 == 0L) "0$minutes" else minutes.toString()
        val sec = if (seconds / 10 == 0L) "0$seconds" else seconds.toString()
        val mil = if (millis / 10 == 0L) "0$millis" else millis.toString()

        _timerTime.value?.apply {
            time = "$min:$sec.$mil"
            hasMinus = (elapsed.sign == -1)
        }
        _timerTime.value = _timerTime.value // will fire observers
    }

    companion object {
        private const val TIMER_UI_UPDATE_FREQ = 50L
        private const val TIMER_UI_UPDATE_DELTA_TIME = 1000 / TIMER_UI_UPDATE_FREQ

        const val TIMER_UI_PATTERN = "00:00.00"
    }
}