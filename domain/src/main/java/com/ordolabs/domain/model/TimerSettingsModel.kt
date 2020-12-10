package com.ordolabs.domain.model

data class TimerSettingsModel(
    val limitMinutes: Int,
    val limitSeconds: Int,
    val player1: String,
    val player2: String
)