package com.ordolabs.domain.usecase.datastore

import com.ordolabs.domain.model.DSStopwatchSettingsModel
import com.ordolabs.domain.repository.IDSStopwatchSettingsRepository
import com.ordolabs.domain.usecase.BaseUsecase
import kotlinx.coroutines.flow.Flow

class SetStopwatchSettingsUseCase(
    private val dsStopwatchSettingsRepository: IDSStopwatchSettingsRepository
) : BaseUsecase<DSStopwatchSettingsModel, Flow<Unit>> {

    override suspend fun invoke(params: DSStopwatchSettingsModel): Flow<Unit> {
        return dsStopwatchSettingsRepository.setStopwatchSettings(params)
    }
}