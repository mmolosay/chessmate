package com.ordolabs.chessmate.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    this.background?.draw(canvas)
    this.draw(canvas)

    return bitmap
}