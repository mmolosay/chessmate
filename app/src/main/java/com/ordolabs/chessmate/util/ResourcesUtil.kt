package com.ordolabs.chessmate.util

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.DimenRes

fun Resources.getFloatDimen(@DimenRes resid: Int): Float {
    val typed = TypedValue()
    this.getValue(resid, typed, true)
    return typed.float
}