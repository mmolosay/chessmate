package com.ordolabs.chessmate.mapper

import com.ordolabs.chessmate.model.StopwatchSettingsPresentation
import com.ordolabs.domain.model.DSStopwatchSettingsModel

internal fun DSStopwatchSettingsModel.toPresentation(): StopwatchSettingsPresentation {
    return StopwatchSettingsPresentation(limitMinutes, limitSeconds, player1, player2)
}