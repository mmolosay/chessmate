package com.ordolabs.chessmate.di

import androidx.room.Room
import com.ordolabs.data.ChessMateDB
import com.ordolabs.data.dao.MovesDao
import com.ordolabs.data.repository.MovesRepository
import com.ordolabs.domain.repository.IDBMovesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DBSourceModule = module {

    single {
        Room
            .databaseBuilder(androidContext(), ChessMateDB::class.java, "chessmate_db")
            .build()
    }

    // DAOs
    single {
        val db: ChessMateDB = get()
        provideMovesDao(db)
    }

    // Repositories
    single<IDBMovesRepository> {
        val dao: MovesDao = get()
        MovesRepository(dao)
    }
}

internal fun provideMovesDao(db: ChessMateDB): MovesDao {
    return db.movesDao
}