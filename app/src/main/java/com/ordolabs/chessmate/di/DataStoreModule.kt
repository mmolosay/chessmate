package com.ordolabs.chessmate.di

import com.ordolabs.data.datastore.ChessMateDS
import com.ordolabs.data.datastore.repository.TimerSettingsRepository
import com.ordolabs.domain.repository.ITimerSettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataStoreModule = module {

    single {
        ChessMateDS(androidContext())
    }

    single<ITimerSettingsRepository> {
        val ds: ChessMateDS = get()
        TimerSettingsRepository(ds)
    }
}