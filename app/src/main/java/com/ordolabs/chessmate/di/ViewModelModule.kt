package com.ordolabs.chessmate.di

import com.ordolabs.chessmate.viewmodel.HomeViewModel
import com.ordolabs.chessmate.viewmodel.StopwatchSettingsViewModel
import com.ordolabs.chessmate.viewmodel.StopwatchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel()
    }

    viewModel {
        StopwatchViewModel()
    }

    viewModel {
        StopwatchSettingsViewModel(
            getStopwatchSettingsUseCase = get(named(UseCase.DS_GET_STOPWATCH_SETTINGS)),
            setStopwatchSettingsUseCase = get(named(UseCase.DS_SET_STOPWATCH_SETTINGS))
        )
    }
}