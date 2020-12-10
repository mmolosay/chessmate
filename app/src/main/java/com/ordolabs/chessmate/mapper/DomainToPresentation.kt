package com.ordolabs.chessmate.mapper

import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.domain.model.TimerSettingsModel

internal fun TimerSettingsModel.toPresentation(): TimerSettingsPresentation {
    return TimerSettingsPresentation(limitMinutes, limitSeconds, player1, player2)
}