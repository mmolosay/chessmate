package com.ordolabs.chessmate.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ordolabs.chessmate.model.TimerData
import com.ordolabs.chessmate.ui.adapter.CheckpointItem
import com.ordolabs.chessmate.ui.view.TimerWarnView
import com.ordolabs.chessmate.util.struct.Timer
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel
import kotlin.math.absoluteValue
import kotlin.math.sign

class TimerViewModel : BaseViewModel() {

    val timerData: LiveData<TimerData>
        get() = _timerData

    val timerState: LiveData<Timer.State>
        get() = _timerState

    val timerCheckpoints: LiveData<List<CheckpointItem>>
        get() = _timerCheckpoints

    val warnState: LiveData<TimerWarnView.State>
        get() = _warnState


    private val _timerData = MutableLiveData(
        TimerData(TIMER_UI_PATTERN, 0, false)
    )
    private val _timerState = MutableLiveData(
        Timer.State.STOPPED
    )
    private val _timerCheckpoints = MutableLiveData<List<CheckpointItem>>(
        mutableListOf()
    )
    private val _warnState = MutableLiveData(
        TimerWarnView.State.HIDDEN
    )


    val isTimerStopped: Boolean
        get() = (timer.state == Timer.State.STOPPED)

    val isTimerPaused: Boolean
        get() = (timer.state == Timer.State.PAUSED)

    val isTimerRunning: Boolean
        get() = (timer.state == Timer.State.RUNNING)


    private lateinit var players: Pair<String, String>

    private val timer = Timer()
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerTick = object : Runnable {
        override fun run() {
            onTimerTick()
            timerHandler.postDelayed(this, TIMER_UI_UPDATE_DELTA_TIME)
        }
    }

    private fun onTimerTick() {
        val remaining = timer.getRemainingTime()
        updateTimerTime(remaining)
        updateWarnState(remaining)
    }

    fun isTimerExpired(): Boolean {
        val remaining = _timerData.value?.rawTime!!
        return (remaining < 0)
    }

    fun setTimerLimit(limit: Long) {
        timer.limit = limit
    }

    fun applyTimerLimit(limit: Long) {
        updateTimerTime(limit)
    }

    fun setPlayerNames(players: Pair<String, String>) {
        this.players = players
    }

    fun addTimerCheckpoint() {
        val remaining = timer.getRemainingTime()
        updateTimerTime(remaining)
        updateWarnState(remaining)
        addTimerCheckpoint(remaining)
    }

    fun clearTimerCheckpoints() {
        _timerCheckpoints.value = mutableListOf()
    }

    fun startTimer() {
        timer.start()
        timerHandler.post(timerTick)

        updateTimerState()
    }

    fun pauseTimer() {
        timerHandler.removeCallbacks(timerTick)
        timer.pause()

        onTimerTick()
        updateTimerState()
    }

    fun resumeTimer() {
        timer.resume()
        timerHandler.post(timerTick)

        updateTimerState()
    }

    fun stopTimer() {
        timerHandler.removeCallbacks(timerTick)
        timer.stop()

        onTimerTick()
        updateTimerState()
    }

    fun restartTimer() {
        if (timer.isPaused) {
            timerHandler.post(timerTick)
        }
        if (!timer.isStopped) {
            timer.restart()
        }

        updateTimerState()
        _timerData.value?.time = TIMER_UI_PATTERN
        _timerData.value?.hasMinus = false
    }

    private fun updateTimerTime(remaining: Long) {
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

    private fun updateTimerState() {
        _timerState.value = timer.state
    }

    private fun addTimerCheckpoint(remaining: Long) {
        val list = _timerCheckpoints.value!! as MutableList
        val size = list.size
        val checkpoint = CheckpointItem(
            ordinal = size + 1,
            playerName = players.first.takeIf { size % 2 == 0 } ?: players.second,
            checkpointTime = _timerData.value!!.time,
            isExpired = (remaining < 0)
        )
        list.add(checkpoint)
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