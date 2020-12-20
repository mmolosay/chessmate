package com.ordolabs.chessmate.util

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.drawRect(left: Int, top: Int, right: Int, bottom: Int, paint: Paint) {
    this.drawRect(
        left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint
    )
}