package com.ordolabs.chessmate.mapper

import com.ordolabs.chessmate.model.StopwatchSettingsPresentation
import com.ordolabs.domain.model.DSStopwatchSettingsModel

internal fun StopwatchSettingsPresentation.toDomain(): DSStopwatchSettingsModel {
    return DSStopwatchSettingsModel(limitMinutes, limitSeconds)
}