package com.ordolabs.chessmate.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ordolabs.chessmate.util.struct.Stopwatch
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel

class TimerViewModel : BaseViewModel() {

    val timerTime: LiveData<String>
        get() = _timerTime

    private val _timerTime = MutableLiveData<String>()

    private val timer = Stopwatch()
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerTick = object : Runnable {
        override fun run() {
            val elapsed = timer.getElapsedTime()
            val string = parseTimerTime(elapsed)
            _timerTime.value = string
            timerHandler.postDelayed(this, TIMER_UI_UPDATE_DELTA_TIME)
        }
    }

    fun isTimerRunning(): Boolean {
        return timer.running
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
        _timerTime.value = TIMER_UI_PATTERN
    }

    private fun parseTimerTime(elapsed: Long): String {
        val minutes = elapsed / 1000 / 60
        val seconds = (elapsed - (minutes * 1000 * 60)) / 1000
        val millis = (elapsed - (minutes * 1000 * 60 + seconds * 1000)) / 10

        val min = if (minutes / 10 == 0L) "0$minutes" else minutes.toString()
        val sec = if (seconds / 10 == 0L) "0$seconds" else seconds.toString()
        val mil = if (millis / 10 == 0L) "0$millis" else millis.toString()

        return "$min:$sec.$mil"
    }

    companion object {
        private const val TIMER_UI_UPDATE_FREQ = 50L
        private const val TIMER_UI_UPDATE_DELTA_TIME = 1000 / TIMER_UI_UPDATE_FREQ

        const val TIMER_UI_PATTERN = "00:00.00"
    }
}