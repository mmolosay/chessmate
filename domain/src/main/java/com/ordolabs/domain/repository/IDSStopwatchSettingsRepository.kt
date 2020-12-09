package com.ordolabs.domain.repository

import com.ordolabs.domain.model.DSStopwatchSettingsModel
import kotlinx.coroutines.flow.Flow

interface IDSStopwatchSettingsRepository {

    fun getStopwatchSettings(): Flow<DSStopwatchSettingsModel>
    suspend fun setStopwatchSettings(settings: DSStopwatchSettingsModel): Flow<Unit>
}