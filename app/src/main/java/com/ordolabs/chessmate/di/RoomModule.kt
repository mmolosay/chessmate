package com.ordolabs.chessmate.di

import androidx.room.Room
import com.ordolabs.data.room.ChessMateDB
import com.ordolabs.data.room.dao.MovesDao
import com.ordolabs.data.room.repository.MovesRepository
import com.ordolabs.domain.repository.IRoomMovesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RoomModule = module {

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
    single<IRoomMovesRepository> {
        val dao: MovesDao = get()
        MovesRepository(dao)
    }
}

internal fun provideMovesDao(db: ChessMateDB): MovesDao {
    return db.movesDao
}