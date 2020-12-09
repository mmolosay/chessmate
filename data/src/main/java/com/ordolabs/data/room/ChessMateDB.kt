package com.ordolabs.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ordolabs.data.room.dao.MovesDao
import com.ordolabs.data.room.entity.MoveEntity

@Database(entities = [MoveEntity::class], version = 1, exportSchema = false)
abstract class ChessMateDB : RoomDatabase() {

    abstract val movesDao: MovesDao
}