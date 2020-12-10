package com.ordolabs.chessmate.di

import com.ordolabs.chessmate.viewmodel.HomeViewModel
import com.ordolabs.chessmate.viewmodel.TimerSettingsViewModel
import com.ordolabs.chessmate.viewmodel.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel()
    }

    viewModel {
        TimerViewModel()
    }

    viewModel {
        TimerSettingsViewModel(
            getTimerSettingsUseCase = get(named(UseCase.GET_TIMER_SETTINGS)),
            setTimerSettingsUseCase = get(named(UseCase.SET_TIMER_SETTINGS))
        )
    }
}