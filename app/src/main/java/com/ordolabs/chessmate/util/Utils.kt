package com.ordolabs.chessmate.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.ordolabs.chessmate.R

object Utils {

    fun vibrate(context: Context?) {
        context ?: return
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val duration = context.resources.getInteger(R.integer.vibration_duration_default).toLong()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(duration)
        }
    }
}