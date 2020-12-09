package com.ordolabs.domain.usecase.datastore

import com.ordolabs.domain.model.DSStopwatchSettingsModel
import com.ordolabs.domain.repository.IDSStopwatchSettingsRepository
import com.ordolabs.domain.usecase.BaseUsecase
import kotlinx.coroutines.flow.Flow

class GetStopwatchSettingsUseCase(
    private val dsStopwatchSettingsRepository: IDSStopwatchSettingsRepository
) : BaseUsecase<Unit, Flow<DSStopwatchSettingsModel>> {

    override suspend fun invoke(params: Unit): Flow<DSStopwatchSettingsModel> {
        return dsStopwatchSettingsRepository.getStopwatchSettings()
    }
}