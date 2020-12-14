package com.ordolabs.chessmate.model

data class TimerData(
    var time: String,
    var rawTime: Long,
    var hasMinus: Boolean
)