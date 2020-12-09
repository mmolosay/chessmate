package com.ordolabs.chessmate.di

import com.ordolabs.data.datastore.StopwatchSettingsDS
import com.ordolabs.data.datastore.repository.DSStopwatchSettingsRepository
import com.ordolabs.domain.repository.IDSStopwatchSettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataStoreModule = module {

    single {
        StopwatchSettingsDS(androidContext())
    }

    single<IDSStopwatchSettingsRepository> {
        val ds: StopwatchSettingsDS = get()
        DSStopwatchSettingsRepository(ds)
    }
}