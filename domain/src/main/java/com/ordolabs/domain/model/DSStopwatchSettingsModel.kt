package com.ordolabs.domain.model

data class DSStopwatchSettingsModel(
    val limitMinutes: Int,
    val limitSeconds: Int,
    val player1: String,
    val player2: String
)