package com.ordolabs.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ordolabs.data.dao.MovesDao
import com.ordolabs.data.entity.MoveEntity

@Database(entities = [MoveEntity::class], version = 1, exportSchema = false)
abstract class ChessMateDB : RoomDatabase() {

    abstract val movesDao: MovesDao
}