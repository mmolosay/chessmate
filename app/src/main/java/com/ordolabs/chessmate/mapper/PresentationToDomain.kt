package com.ordolabs.chessmate.mapper

import com.ordolabs.chessmate.model.TimerSettingsPresentation
import com.ordolabs.domain.model.TimerSettingsModel

internal fun TimerSettingsPresentation.toDomain(): TimerSettingsModel {
    return TimerSettingsModel(limitMinutes, limitSeconds, player1, player2)
}