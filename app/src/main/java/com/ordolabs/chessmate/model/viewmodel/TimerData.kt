package com.ordolabs.chessmate.model.viewmodel

data class TimerData(
    var time: String,
    var rawTime: Long,
    var hasMinus: Boolean
)