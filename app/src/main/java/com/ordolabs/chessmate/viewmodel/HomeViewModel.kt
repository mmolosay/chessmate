package com.ordolabs.chessmate.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ordolabs.chessmate.util.struct.Stopwatch
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val stopwatchTime: LiveData<String>
        get() = _stopwatchTime

    private val _stopwatchTime = MutableLiveData<String>()

    private val stopwatch = Stopwatch()
    private val stopwatchHandler = Handler(Looper.getMainLooper())
    private val stopwatchTick = object : Runnable {
        override fun run() {
            val elapsed = stopwatch.getElapsedTime()
            val string = parseStopwatchTime(elapsed)
            _stopwatchTime.value = string
            stopwatchHandler.postDelayed(this, STOPWATCH_UPDATE_DELTA_TIME)
        }
    }

    fun isStopwatchRunning(): Boolean {
        return stopwatch.running
    }

    fun startStopwatch() {
        stopwatch.start()
        stopwatchHandler.post(stopwatchTick)
    }

    fun stopStopwatch() {
        stopwatch.stop()
        stopwatchHandler.removeCallbacks(stopwatchTick)
    }

    fun resetStopwatch() {
        if (stopwatch.running) {
            stopwatch.restart()
        }
        _stopwatchTime.value = STOPWATCH_PATTERN
    }

    private fun parseStopwatchTime(elapsed: Long): String {
        val minutes = elapsed / 1000 / 60
        val seconds = (elapsed - (minutes * 1000 * 60)) / 1000
        val millis = (elapsed - (minutes * 1000 * 60 + seconds * 1000)) / 10

        val min = if (minutes / 10 == 0L) "0$minutes" else minutes.toString()
        val sec = if (seconds / 10 == 0L) "0$seconds" else seconds.toString()
        val mil = if (millis / 10 == 0L) "0$millis" else millis.toString()

        return "$min:$sec.$mil"
    }

    companion object {
        private const val STOPWATCH_UPDATE_FREQ = 50L
        private const val STOPWATCH_UPDATE_DELTA_TIME = 1000 / STOPWATCH_UPDATE_FREQ

        const val STOPWATCH_PATTERN = "00:00.00"
    }
}