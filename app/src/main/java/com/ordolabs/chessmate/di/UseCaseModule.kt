package com.ordolabs.chessmate.di

import com.ordolabs.domain.repository.ITimerSettingsRepository
import com.ordolabs.domain.usecase.datastore.GetTimerSettingsUseCase
import com.ordolabs.domain.usecase.datastore.SetTimerSettingsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {

    single(named(UseCase.GET_TIMER_SETTINGS)) {
        val repo: ITimerSettingsRepository = get()
        provideGetTimerSettingsUseCase(repo)
    }

    single(named(UseCase.SET_TIMER_SETTINGS)) {
        val repo: ITimerSettingsRepository = get()
        provideSetTimerSettingsUseCase(repo)
    }
}

internal enum class UseCase {
    GET_TIMER_SETTINGS,
    SET_TIMER_SETTINGS
}

fun provideGetTimerSettingsUseCase(
    timerSettingsRepository: ITimerSettingsRepository
): GetTimerSettingsUseCase {
    return GetTimerSettingsUseCase(timerSettingsRepository)
}

fun provideSetTimerSettingsUseCase(
    timerSettingsRepository: ITimerSettingsRepository
): SetTimerSettingsUseCase {
    return SetTimerSettingsUseCase(timerSettingsRepository)
}