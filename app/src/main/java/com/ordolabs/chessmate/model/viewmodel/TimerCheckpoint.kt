package com.ordolabs.chessmate.model.viewmodel

data class TimerCheckpoint(
    var playerOrdinal: Int,
    var checkpointTime: String,
    var checkpointOrdinal: Int,
    var isExpired: Boolean
)