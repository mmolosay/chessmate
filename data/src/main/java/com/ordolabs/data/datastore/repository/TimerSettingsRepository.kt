package com.ordolabs.data.datastore.repository

import com.ordolabs.data.datastore.ChessMateDS
import com.ordolabs.domain.model.TimerSettingsModel
import com.ordolabs.domain.repository.ITimerSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimerSettingsRepository(
    private val ds: ChessMateDS
) : ITimerSettingsRepository {

    override fun getTimerSettings(): Flow<TimerSettingsModel> {
        return ds.getTimerSettings()
    }

    override suspend fun setTimerSettings(settings: TimerSettingsModel): Flow<Unit> {
        ds.setTimerSettings(settings)
        return flow { emit(Unit) }
    }
}