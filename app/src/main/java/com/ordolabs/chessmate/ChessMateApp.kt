package com.ordolabs.chessmate

import android.app.Application
import android.content.Context
import com.ordolabs.chessmate.di.DBSourceModule
import com.ordolabs.chessmate.di.singletonsModule
import com.ordolabs.chessmate.di.useCaseModule
import com.ordolabs.chessmate.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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
                DBSourceModule,
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