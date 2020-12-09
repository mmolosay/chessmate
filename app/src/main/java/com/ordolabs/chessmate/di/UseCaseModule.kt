package com.ordolabs.chessmate.di

import com.ordolabs.domain.repository.IDSStopwatchSettingsRepository
import com.ordolabs.domain.usecase.datastore.GetStopwatchSettingsUseCase
import com.ordolabs.domain.usecase.datastore.SetStopwatchSettingsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {

    single(named(UseCase.DS_GET_STOPWATCH_SETTINGS)) {
        val repo: IDSStopwatchSettingsRepository = get()
        provideGetStopwatchSettingsUseCase(repo)
    }

    single(named(UseCase.DS_SET_STOPWATCH_SETTINGS)) {
        val repo: IDSStopwatchSettingsRepository = get()
        provideSetStopwatchSettingsUseCase(repo)
    }
}

internal enum class UseCase {
    DS_GET_STOPWATCH_SETTINGS,
    DS_SET_STOPWATCH_SETTINGS
}

fun provideGetStopwatchSettingsUseCase(
    dsStopwatchSettingsRepository: IDSStopwatchSettingsRepository
): GetStopwatchSettingsUseCase {
    return GetStopwatchSettingsUseCase(dsStopwatchSettingsRepository)
}

fun provideSetStopwatchSettingsUseCase(
    dsStopwatchSettingsRepository: IDSStopwatchSettingsRepository
): SetStopwatchSettingsUseCase {
    return SetStopwatchSettingsUseCase(dsStopwatchSettingsRepository)
}