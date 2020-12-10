package com.ordolabs.domain.repository

import com.ordolabs.domain.model.TimerSettingsModel
import kotlinx.coroutines.flow.Flow

interface ITimerSettingsRepository {

    fun getTimerSettings(): Flow<TimerSettingsModel>
    suspend fun setTimerSettings(settings: TimerSettingsModel): Flow<Unit>
}