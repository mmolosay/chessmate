package com.ordolabs.data.datastore.repository

import com.ordolabs.data.datastore.StopwatchSettingsDS
import com.ordolabs.domain.model.DSStopwatchSettingsModel
import com.ordolabs.domain.repository.IDSStopwatchSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DSStopwatchSettingsRepository(
    private val stopwatchSettingsDS: StopwatchSettingsDS
) : IDSStopwatchSettingsRepository {

    override fun getStopwatchSettings(): Flow<DSStopwatchSettingsModel> {
        return stopwatchSettingsDS.getStopwatchSettings()
    }

    override suspend fun setStopwatchSettings(settings: DSStopwatchSettingsModel): Flow<Unit> {
        stopwatchSettingsDS.setStopwatchSettings(settings)
        return flow { emit(Unit) }
    }
}