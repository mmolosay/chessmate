package com.ordolabs.domain.usecase.datastore

import com.ordolabs.domain.model.TimerSettingsModel
import com.ordolabs.domain.repository.ITimerSettingsRepository
import com.ordolabs.domain.usecase.BaseUsecase
import kotlinx.coroutines.flow.Flow

class GetTimerSettingsUseCase(
    private val timerSettingsRepository: ITimerSettingsRepository
) : BaseUsecase<Unit, Flow<TimerSettingsModel>> {

    override suspend fun invoke(params: Unit): Flow<TimerSettingsModel> {
        return timerSettingsRepository.getTimerSettings()
    }
}