package com.ordolabs.chessmate

import android.app.Application
import android.content.Context
import com.ordolabs.chessmate.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// TODO: add landscape layouts
class ChessMateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        setKoin()
    }

    private fun setKoin() {
        startKoin {
            androidContext(this@ChessMateApp)
            modules(
                RoomModule,
                DataStoreModule,
                useCaseModule,
                viewModelModule,
                singletonsModule
            )
        }
    }

    companion object {
        lateinit var context: Context
    }
}