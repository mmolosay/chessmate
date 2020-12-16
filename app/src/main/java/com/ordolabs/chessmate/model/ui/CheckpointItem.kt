package com.ordolabs.chessmate.model.ui

data class CheckpointItem(
    val playerName: String,
    val checkpointTime: String,
    val ordinal: String,
    val isExpired: Boolean
)