package com.ordolabs.chessmate.util.struct

/**
 * Actually, it is a stopwatch, because it runs forward from zero time.
 */
class Timer {

    var state: State = State.STOPPED
        private set

    val isStopped: Boolean
        get() = (state == State.STOPPED)
    val isPaused: Boolean
        get() = (state == State.PAUSED)
    val isRunning: Boolean
        get() = (state == State.RUNNING)

    enum class State {
        STOPPED, PAUSED, RUNNING
    }

    /**
     * An amout of time in milliseconds, after passing which
     * timer would be considered as expired.
     */
    var limit: Long = TIME_UNKNOWN

    private var totalTime: Long = 0
    private var startTime: Long = TIME_UNKNOWN

    fun start() {
        if (limit == TIME_UNKNOWN)
            throw IllegalStateException("limit must be set before calling start()")

        reset()
        startTime = System.currentTimeMillis()
        state = State.RUNNING
    }

    fun pause() {
        if (!isRunning)
            throw IllegalTimerStateException("pause")

        totalTime += System.currentTimeMillis() - startTime

        state = State.PAUSED
    }

    fun resume() {
        if (!isPaused)
            throw IllegalTimerStateException("resume")

        startTime = System.currentTimeMillis()
        state = State.RUNNING
    }

    fun stop() {
        if (isStopped)
            throw IllegalTimerStateException("stop")

        if (!isPaused) {
            totalTime += System.currentTimeMillis() - startTime
        }
        state = State.STOPPED
    }

    fun restart() {
        if (isStopped)
            throw IllegalTimerStateException("restart")

        start()
    }

    fun getElapsedTime(): Long = synchronized(this) {
        if (startTime == TIME_UNKNOWN)
            throw IllegalStateException("Timer must be started at least once")

        when (state) {
            State.RUNNING -> (System.currentTimeMillis() - startTime) + totalTime
            State.PAUSED -> totalTime
            State.STOPPED -> 0
        }
    }

    fun getRemainingTime(): Long = synchronized(this) {
        limit - getElapsedTime()
    }

    private fun reset() {
        totalTime = 0
        startTime = TIME_UNKNOWN
    }

    companion object {
        private const val TIME_UNKNOWN: Long = -1
    }

    inner class IllegalTimerStateException(methodThrew: String) : IllegalStateException() {
        override val message: String =
            "Calling $methodThrew() method is illegal while timer is in ${state.name} state"
    }
}