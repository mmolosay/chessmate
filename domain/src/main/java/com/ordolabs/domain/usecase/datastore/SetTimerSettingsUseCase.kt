package com.ordolabs.domain.usecase.datastore

import com.ordolabs.domain.model.TimerSettingsModel
import com.ordolabs.domain.repository.ITimerSettingsRepository
import com.ordolabs.domain.usecase.BaseUsecase
import kotlinx.coroutines.flow.Flow

class SetTimerSettingsUseCase(
    private val timerSettingsRepository: ITimerSettingsRepository
) : BaseUsecase<TimerSettingsModel, Flow<Unit>> {

    override suspend fun invoke(params: TimerSettingsModel): Flow<Unit> {
        return timerSettingsRepository.setTimerSettings(params)
    }
}