package com.ordolabs.chessmate.mapper

import android.content.Context
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.chessmate.model.ui.CheckpointItem
import com.ordolabs.chessmate.model.viewmodel.TimerCheckpoint

fun TimerCheckpoint.toUI(
    context: Context,
    settings: TimerSettingsPresentation
): CheckpointItem {
    return CheckpointItem(
        playerName = settings.player1.takeIf { this.playerOrdinal == 0 } ?: settings.player2,
        checkpointTime = this.checkpointTime,
        ordinal = context.getString(R.string.item_checkpoint_ordinal, this.checkpointOrdinal),
        isExpired = this.isExpired
    )
}