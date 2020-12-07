package com.ordolabs.chessmate.util.struct

/**
 * Stopwatch
 */
class Stopwatch {

    var running: Boolean = false
        private set

    private var startTime: Long = TIME_UNKNOWN
    private var stopTime: Long = TIME_UNKNOWN

    fun start() {
        reset()
        startTime = System.currentTimeMillis()
        running = true
    }

    fun stop() {
        stopTime = System.currentTimeMillis()
        running = false
    }

    fun restart() {
        reset()
        start()
    }

    fun getElapsedTime(): Long = synchronized(this) {
        if (running) {
            System.currentTimeMillis() - startTime
        } else {
            stopTime - startTime
        }
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