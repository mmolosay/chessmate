package com.ordolabs.chessmate.model.presentation

data class TimerSettingsPresentation(
    val limitMinutes: Int,
    val limitSeconds: Int,
    val player1: String,
    val player2: String
)