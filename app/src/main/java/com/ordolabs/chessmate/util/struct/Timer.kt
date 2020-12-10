package com.ordolabs.chessmate.util.struct

/**
 * Actually, it is a stopwatch, because it runs forward from zero time.
 */
class Timer {

    var running: Boolean = false
        private set

    /**
     * An amout of time in milliseconds, after passing which
     * timer would be considered as expired.
     */
    var limit: Long = TIME_UNKNOWN

    private var startTime: Long = TIME_UNKNOWN
    private var stopTime: Long = TIME_UNKNOWN

    fun start() {
        if (limit == TIME_UNKNOWN)
            throw IllegalStateException("limit must be set before calling start()")
        if (running)
            throw IllegalStateException("Timer must be stopped to call start()")

        reset()
        startTime = System.currentTimeMillis()
        running = true
    }

    fun stop() {
        if (!running)
            throw IllegalStateException("Timer must be running to cal stop()")

        stopTime = System.currentTimeMillis()
        running = false
    }

    fun restart() {
        if (!running)
            throw IllegalStateException("Timer must be running to call restart()")

        reset()
        start()
    }

    fun getElapsedTime(): Long = synchronized(this) {
        if (startTime == TIME_UNKNOWN)
            throw IllegalStateException("Timer must be started at least once")

        if (running) {
            System.currentTimeMillis() - startTime
        } else {
            stopTime - startTime
        }
    }

    fun getRemainingTime(): Long = synchronized(this) {
        limit - getElapsedTime()
    }

    private fun reset() {
        startTime = TIME_UNKNOWN
        stopTime = TIME_UNKNOWN
        running = false
    }

    companion object {
        private const val TIME_UNKNOWN: Long = -1
    }
}